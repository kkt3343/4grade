#include "201701531_kkt_HW2.h"

// 전역변수 선언
//메세지 박스 여러번 띄우기 방지 코드
boolean locked = false;

//윈도우의 크기 설정
int windows_width = 600;
int windows_height = 900;

float start_width = windows_width / 2;
float start_height = 100;

/*현재 마우스 확인 포인트*/
D2D_POINT_2F mouse_point;
D2D_POINT_2F last_mouse_point;

//각도 및 사이즈 출력
float Angle = 0;
float Size = 0;

//마우스의 상태 확인
boolean isdrag;
boolean isinsert;
boolean isdelete;

boolean isinsert_inner;
boolean isdelete_inner;

//시작 사각형의 좌표 (회색 상자)
//시작상자의 크기 설정
//항상 높이와 너비는 1:2 이여야 한다.
int start_box_height = 50;
int start_box_width = start_box_height * 2;
struct startbox {
	float left = windows_width / 2 - start_box_width / 2; // 250
	float top = 100 - start_box_height/2; //75
	float right = windows_width / 2 + start_box_width / 2; // 350
	float bottom = 100 + start_box_height / 2; //125
};
startbox start_box;

//학생상자의 크기 설정
//항상 높이와 너비는 1:4 이여야 한다.
int people_box_height = 50;
int people_box_width = people_box_height * 4;
int padding = 5;

//하단의 시작점은 윈도우 창의 -100

struct stackbox {
	float left = windows_width / 2 - people_box_width / 2;
	float top = windows_height - 100 - people_box_height;
	float right = windows_width / 2 + people_box_width / 2;
	float bottom = windows_height - 100;
};
stackbox stack_box;

//박스의 최상단의 위치를 계산한다.
struct nextbox {
	float left = windows_width / 2 - people_box_width / 2;
	float top;
	float right = windows_width / 2 + people_box_width / 2;
	float bottom;
};

nextbox present_coordinate(int stackSize) {
	//학생 스택을 기반으로 좌표를 구한다.
	nextbox tmp;
	tmp.top = stack_box.top - (people_box_height + padding) * stackSize;
	tmp.bottom = stack_box.bottom - (people_box_height + padding) * stackSize;
	return tmp;
}

student student_tmp;

// 응용 프로그램의 진입점 함수
int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE /*hPrevInstance*/, LPSTR /*lpCmdLine*/, int /*nCmdShow*/)
{
	if (SUCCEEDED(CoInitialize(NULL)))
	{
		{
			DemoApp app;
			if (SUCCEEDED(app.Initialize(hInstance)))
			{
				app.RunMessageLoop();
			}
		}
		CoUninitialize();
	}
	return 0;
}

// 생성자. 멤버 변수들을 초기화함.
DemoApp::DemoApp():
	m_hwnd(NULL),
	m_pDirect2dFactory(NULL),
	m_pRenderTarget(NULL),
	m_pLightSlateGrayBrush(NULL),
	m_pCornflowerBlueBrush(NULL),

	//테두리
	m_border_brush(NULL),

	//브러쉬
	m_box_brush01(NULL),
	m_box_brush02(NULL),
	m_box_brush03(NULL),
	m_box_brush04(NULL),
	m_box_brush05(NULL),
	m_box_brush06(NULL),
	m_box_brush07(NULL),
	m_box_brush08(NULL),
	m_box_brush09(NULL),
	m_box_brush10(NULL),

	//투명박스
	m_pTransparentBox(NULL),

	m_pTextBrush(NULL),

	//글씨
	m_pDWriteFactory(NULL),
	m_pTextFormat(NULL)
{
}

// 소멸자. 응용 프로그램의 자원을 반납함.
DemoApp::~DemoApp()
{
	//밑의 함수에서 지우기
	DiscardDeviceResources();
	SAFE_RELEASE(m_pDirect2dFactory);

	//글씨
	SAFE_RELEASE(m_pTextBrush);
	SAFE_RELEASE(m_pDWriteFactory);
	SAFE_RELEASE(m_pTextFormat);
}

// 응용 프로그램의 원도우를 생성함. 장치 독립적 자원을 생성함.
HRESULT DemoApp::Initialize(HINSTANCE hInstance)
{
	HRESULT hr;

	// 장치 독립적 자원을 생성함.
	hr = CreateDeviceIndependentResources();

	if (SUCCEEDED(hr))
	{
		// 윈도우 클래스를 등록함..
		WNDCLASSEX wcex = { sizeof(WNDCLASSEX) };
		wcex.style = CS_HREDRAW | CS_VREDRAW;
		wcex.lpfnWndProc = DemoApp::WndProc;
		wcex.cbClsExtra = 0;
		wcex.cbWndExtra = sizeof(LONG_PTR);
		wcex.hInstance = hInstance;
		wcex.hbrBackground = NULL;
		wcex.lpszMenuName = NULL;
		wcex.hCursor = LoadCursor(NULL, IDI_APPLICATION);
		wcex.lpszClassName = L"D2DDemoApp";
		RegisterClassEx(&wcex);

		// 윈도우를 생성함. (Title명)
		m_hwnd = CreateWindow(
			L"D2DDemoApp", L"201701531_김기태_HW2",
			WS_OVERLAPPEDWINDOW, CW_USEDEFAULT, CW_USEDEFAULT,
			windows_width, windows_height, NULL, NULL, hInstance, this
		);
		hr = m_hwnd ? S_OK : E_FAIL;
		if (SUCCEEDED(hr))
		{
			ShowWindow(m_hwnd, SW_SHOWNORMAL);
			UpdateWindow(m_hwnd);
		}
	}

	return hr;
}

// 장치 독립적 자원들을 생성함. 이들 자원의 수명은 응용 프로그램이 종료되기 전까지 유효함.
HRESULT DemoApp::CreateDeviceIndependentResources()
{
	static const WCHAR msc_fontName[] = L"Verdana";
	static const FLOAT msc_fontSize = 50;

	HRESULT hr = S_OK;

	// D2D 팩토리를 생성함.
	hr = D2D1CreateFactory(D2D1_FACTORY_TYPE_SINGLE_THREADED, &m_pDirect2dFactory);

	if (SUCCEEDED(hr))
	{
		// Create a shared DirectWrite factory
		hr = DWriteCreateFactory(
			DWRITE_FACTORY_TYPE_SHARED,
			__uuidof(IDWriteFactory),
			reinterpret_cast<IUnknown**>(&m_pDWriteFactory)
		);
	}

	if (SUCCEEDED(hr))
	{
		// Create a DirectWrite text format object.
		hr = m_pDWriteFactory->CreateTextFormat(
			L"Verdana",     // The font family name.
			NULL,           // The font collection (NULL sets it to use the system font collection).
			DWRITE_FONT_WEIGHT_REGULAR,
			DWRITE_FONT_STYLE_NORMAL,
			DWRITE_FONT_STRETCH_NORMAL,
			13.0f,
			L"ko-kr",
			&m_pTextFormat
		);
	}
	return hr;
}


// 장치 의존적 자원들을 생성함. 장치가 소실되는 경우에는 이들 자원을 다시 생성해야 함.
HRESULT DemoApp::CreateDeviceResources()
{
	HRESULT hr = S_OK;
	// Create a Direct2D factory.
	
	if (!m_pRenderTarget)
	{
		RECT rc;
		GetClientRect(m_hwnd, &rc);

		D2D1_SIZE_U size = D2D1::SizeU(rc.right - rc.left, rc.bottom - rc.top);

		// D2D 렌더타겟을 생성함.
		hr = m_pDirect2dFactory->CreateHwndRenderTarget(D2D1::RenderTargetProperties(), D2D1::HwndRenderTargetProperties(m_hwnd, size), &m_pRenderTarget);

		if (SUCCEEDED(hr))
		{
			// 회색 붓을 생성함. //격자를 그리는 용도
			hr = m_pRenderTarget->CreateSolidColorBrush(D2D1::ColorF(D2D1::ColorF::LightGray), &m_pLightSlateGrayBrush);
		}
		if (SUCCEEDED(hr))
		{
			// 다음번에 놓일 위치 붓(상자)
			hr = m_pRenderTarget->CreateSolidColorBrush(D2D1::ColorF(D2D1::ColorF::CornflowerBlue), &m_pCornflowerBlueBrush);
		}
		if (SUCCEEDED(hr))
		{
			// 상단의 상자 밑 드래그 할 때 움직이는 상자를 위한 붓
			hr = m_pRenderTarget->CreateSolidColorBrush(D2D1::ColorF(D2D1::ColorF::Gray), &m_pTransparentBox);
		}
		if (SUCCEEDED(hr))
		{
			// 테두리
			hr = m_pRenderTarget->CreateSolidColorBrush(D2D1::ColorF(D2D1::ColorF::Black), &m_border_brush);
		}
		//브러쉬 설정
		if (SUCCEEDED(hr))
		{
			// 박스 붓 설정
			hr = m_pRenderTarget->CreateSolidColorBrush(D2D1::ColorF(D2D1::ColorF::Red), &m_box_brush01);
			hr = m_pRenderTarget->CreateSolidColorBrush(D2D1::ColorF(D2D1::ColorF::Orange), &m_box_brush02);
			hr = m_pRenderTarget->CreateSolidColorBrush(D2D1::ColorF(D2D1::ColorF::Yellow), &m_box_brush03);
			hr = m_pRenderTarget->CreateSolidColorBrush(D2D1::ColorF(D2D1::ColorF::Green), &m_box_brush04);
			hr = m_pRenderTarget->CreateSolidColorBrush(D2D1::ColorF(D2D1::ColorF::LightBlue), &m_box_brush05);
			hr = m_pRenderTarget->CreateSolidColorBrush(D2D1::ColorF(D2D1::ColorF::CadetBlue), &m_box_brush06);
			hr = m_pRenderTarget->CreateSolidColorBrush(D2D1::ColorF(D2D1::ColorF::MediumPurple), &m_box_brush07);
			hr = m_pRenderTarget->CreateSolidColorBrush(D2D1::ColorF(D2D1::ColorF::LightPink), &m_box_brush08);
			hr = m_pRenderTarget->CreateSolidColorBrush(D2D1::ColorF(D2D1::ColorF::Olive), &m_box_brush09);
			hr = m_pRenderTarget->CreateSolidColorBrush(D2D1::ColorF(D2D1::ColorF::Aqua), &m_box_brush10);
		}
		if (SUCCEEDED(hr))
		{
			//글씨
			// Create a solid color brush for writing text.
			hr = m_pRenderTarget->CreateSolidColorBrush(
				D2D1::ColorF(D2D1::ColorF::Black),
				&m_pTextBrush
			);
		}
	}
	return hr;
}

// 장치 의존적 자원들을 반납함. 장치가 소실되면 이들 자원을 다시 생성해야 함.
void DemoApp::DiscardDeviceResources()
{
	SAFE_RELEASE(m_pRenderTarget);
	SAFE_RELEASE(m_pLightSlateGrayBrush);
	SAFE_RELEASE(m_pCornflowerBlueBrush);
	//테두리
	SAFE_RELEASE(m_border_brush);

	//브러쉬
	SAFE_RELEASE(m_box_brush01);
	SAFE_RELEASE(m_box_brush02);
	SAFE_RELEASE(m_box_brush03);
	SAFE_RELEASE(m_box_brush04);
	SAFE_RELEASE(m_box_brush05);
	SAFE_RELEASE(m_box_brush06);
	SAFE_RELEASE(m_box_brush07);
	SAFE_RELEASE(m_box_brush08);
	SAFE_RELEASE(m_box_brush09);
	SAFE_RELEASE(m_box_brush10);

	//투명박스
	SAFE_RELEASE(m_pTransparentBox);
}

// 메인 윈도우 메시지 루프를 실행함.
void DemoApp::RunMessageLoop()
{
	MSG msg;

	while (GetMessage(&msg, NULL, 0, 0))
	{
		TranslateMessage(&msg);
		DispatchMessage(&msg);
	}
}

//학생
student people;
std::vector<student> people_vector;
void create_student()
{
	int family_random = rand() % (sizeof(familyNames) / sizeof(std::string)); //0 ~ 17
	int first_random1 = rand() % (sizeof(firstNames) / sizeof(std::string)); //0 ~ 21
	int first_random2 = rand() % (sizeof(firstNames) / sizeof(std::string)); //0 ~ 21

	//이름설정
	student_tmp.name = familyNames[family_random] + firstNames[first_random1] + firstNames[first_random2];
	//성적설정
	student_tmp.score = rand() % 61 + 30;

	////추가
	//people_vector.push_back(people);
}

void insert_student()
{
	//추가
	people_vector.push_back(student_tmp);
}

void delete_student()
{
	//우선 맨 뒤를 백업을 해두고 만약에 드래그가 중간에 끊기면 복원하고 그렇지 않으면 버린다.
	student_tmp = people_vector.back();
	people_vector.pop_back();
}

//string -> wstring으로 변환을 위한 함수
std::wstring strconv(const std::string& _src)
{
	USES_CONVERSION;
	return std::wstring(A2W(_src.c_str()));

};

//학생상자 생성
void DemoApp::draw_box()
{
	float square_t = stack_box.top;
	float square_b = stack_box.bottom;

	D2D1_RECT_F rectangle_tmp;
	for (int i = 0; i < people_vector.size(); i++) {
		rectangle_tmp = D2D1::RectF(stack_box.left, square_t - (people_box_height + padding) * i, stack_box.right, square_b - (people_box_height + padding) * i);
		if (i == 0) {
			//사각형 내부 색칠
			m_pRenderTarget->FillRoundedRectangle(D2D1::RoundedRect(rectangle_tmp, 10, 10), m_box_brush01);
		}
		else if (i == 1) {
			m_pRenderTarget->FillRoundedRectangle(D2D1::RoundedRect(rectangle_tmp, 10, 10), m_box_brush02);
		}
		else if (i == 2) {
			m_pRenderTarget->FillRoundedRectangle(D2D1::RoundedRect(rectangle_tmp, 10, 10), m_box_brush03);
		}
		else if (i == 3) {
			m_pRenderTarget->FillRoundedRectangle(D2D1::RoundedRect(rectangle_tmp, 10, 10), m_box_brush04);
		}
		else if (i == 4) {
			m_pRenderTarget->FillRoundedRectangle(D2D1::RoundedRect(rectangle_tmp, 10, 10), m_box_brush05);
		}
		else if (i == 5) {
			m_pRenderTarget->FillRoundedRectangle(D2D1::RoundedRect(rectangle_tmp, 10, 10), m_box_brush06);
		}
		else if (i == 6) {
			m_pRenderTarget->FillRoundedRectangle(D2D1::RoundedRect(rectangle_tmp, 10, 10), m_box_brush07);
		}
		else if (i == 7) {
			m_pRenderTarget->FillRoundedRectangle(D2D1::RoundedRect(rectangle_tmp, 10, 10), m_box_brush08);
		}
		else if (i == 8) {
			m_pRenderTarget->FillRoundedRectangle(D2D1::RoundedRect(rectangle_tmp, 10, 10), m_box_brush09);
		}
		else{
			m_pRenderTarget->FillRoundedRectangle(D2D1::RoundedRect(rectangle_tmp, 10, 10), m_box_brush10);
		}
		//사각형 테두리 색칠
		m_pRenderTarget->DrawRoundedRectangle(D2D1::RoundedRect(rectangle_tmp, 10, 10), m_border_brush, 2.0f);
		//학생 정보를 표기
		m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
		WCHAR mouse_text[100];
		
		std::string tmp = people_vector[i].name + "\t" + std::to_string(people_vector[i].score) + "점";
		std::wstring wtmp = strconv(tmp);
		const WCHAR* t = wtmp.c_str();

		swprintf_s(mouse_text, t);
		m_pRenderTarget->DrawText(
			mouse_text,
			wcslen(mouse_text),
			m_pTextFormat,
			D2D1::RectF(stack_box.left + 30, square_t - (people_box_height + padding) * i + 10, stack_box.right - 30, square_b - (people_box_height + padding) * i),
			m_pTextBrush
		); //좌상단에 쓰여지는 정보 쓰기
	}
}

//다음번에 받을 위치
void DemoApp::draw_next_box()
{
	nextbox n = present_coordinate(people_vector.size());

	D2D1_RECT_F rectangle_tmp = D2D1::RectF(n.left,n.top,n.right,n.bottom);
	D2D1_ROUNDED_RECT draw_box_input = D2D1::RoundedRect(rectangle_tmp, 10, 10);
	m_pRenderTarget->FillRoundedRectangle(&draw_box_input, m_pCornflowerBlueBrush);
}

//드래그하면서 그릴 상자를 만드는 함수
void DemoApp::draw_moving_Rectangle() {

	//시작 사격형
	D2D1_RECT_F rectangle_tmp = D2D1::RectF(start_box.left, start_box.top, start_box.right, start_box.bottom);
	D2D1_ROUNDED_RECT draw_box_input = D2D1::RoundedRect(rectangle_tmp, 10, 10);

	nextbox t = present_coordinate(people_vector.size() + 1);
	float tt = (t.top - 20);

	//계산방법
	//시작점에서 크기를 키우고, 회전을 시킨 것을 이동 한다.
	//그래서 scale 과 rotation은 상자를 만드는 지점에서 처리한다.

	//1배 ~ 2배
	Size = (mouse_point.y - last_mouse_point.y) / (tt) + 1.0f;
	Angle = ((mouse_point.y - last_mouse_point.y) / (tt)) * 360.0f;
	
	D2D1_MATRIX_3X2_F scale = D2D1::Matrix3x2F::Scale(D2D1::Size(Size, 1.0f), D2D1::Point2F((start_box.left + start_box.right)/2 , (start_box.top + start_box.bottom) / 2));
	D2D1_MATRIX_3X2_F rotation = D2D1::Matrix3x2F::Rotation(Angle, D2D1::Point2F((start_box.left + start_box.right) / 2, (start_box.top + start_box.bottom) / 2));
	D2D1_MATRIX_3X2_F translation = D2D1::Matrix3x2F::Translation((mouse_point.x - last_mouse_point.x), (mouse_point.y - last_mouse_point.y));
	
	m_pRenderTarget->SetTransform(scale * rotation * translation); // scale->rotation->translation 순
	m_pRenderTarget->DrawRoundedRectangle(draw_box_input, m_border_brush, 1.0f);

	//
	int i = people_vector.size();
	if (i == 0) {
		//사각형 내부 색칠
		m_box_brush01->SetOpacity(0.5);
		m_pRenderTarget->FillRoundedRectangle(draw_box_input, m_box_brush01);
		m_box_brush01->SetOpacity(1.0);
	}
	else if (i == 1) {
		m_box_brush02->SetOpacity(0.5);
		m_pRenderTarget->FillRoundedRectangle(draw_box_input, m_box_brush02);
		m_box_brush02->SetOpacity(1.0);
	}
	else if (i == 2) {
		m_box_brush03->SetOpacity(0.5);
		m_pRenderTarget->FillRoundedRectangle(draw_box_input, m_box_brush03);
		m_box_brush03->SetOpacity(1.0);
	}
	else if (i == 3) {
		m_box_brush04->SetOpacity(0.5);
		m_pRenderTarget->FillRoundedRectangle(draw_box_input, m_box_brush04);
		m_box_brush04->SetOpacity(1.0);
	}
	else if (i == 4) {
		m_box_brush05->SetOpacity(0.5);
		m_pRenderTarget->FillRoundedRectangle(draw_box_input, m_box_brush05);
		m_box_brush05->SetOpacity(1.0);
	}
	else if (i == 5) {
		m_box_brush06->SetOpacity(0.5);
		m_pRenderTarget->FillRoundedRectangle(draw_box_input, m_box_brush06);
		m_box_brush06->SetOpacity(1.0);
	}
	else if (i == 6) {
		m_box_brush07->SetOpacity(0.5);
		m_pRenderTarget->FillRoundedRectangle(draw_box_input, m_box_brush07);
		m_box_brush07->SetOpacity(1.0);
	}
	else if (i == 7) {
		m_box_brush08->SetOpacity(0.5);
		m_pRenderTarget->FillRoundedRectangle(draw_box_input, m_box_brush08);
		m_box_brush08->SetOpacity(1.0);
	}
	else if (i == 8) {
		m_box_brush09->SetOpacity(0.5);
		m_pRenderTarget->FillRoundedRectangle(draw_box_input, m_box_brush09);
		m_box_brush09->SetOpacity(1.0);
	}
	else {
		m_box_brush10->SetOpacity(0.5);
		m_pRenderTarget->FillRoundedRectangle(draw_box_input, m_box_brush10);
		m_box_brush10->SetOpacity(1.0);
	}

	//데이터정보 표시
	m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
	WCHAR mouse_text[100];
	std::string tmp = student_tmp.name + "\t" + std::to_string(student_tmp.score) + "점";
	std::wstring wtmp = strconv(tmp);
	const WCHAR* tp = wtmp.c_str();

	//글씨도 이동을 시키되, scale은 하지 않는다.
	m_pRenderTarget->SetTransform(rotation * translation);

	swprintf_s(mouse_text, tp);
	m_pRenderTarget->DrawText(
		mouse_text,
		wcslen(mouse_text),
		m_pTextFormat,
		D2D1::RectF(start_box.left, start_box.top + 10, start_box.right, start_box.bottom + 10),
		m_pTextBrush
	);
}

// 클라이언트 윈도우 화면을 그림.
// 참고: 이 함수는 실행되는 동안에 장치가 소실되면 장치 의존적 자원들을 반납함. 그 다음 호출 시에 그 자원들을 다시 생성함.
HRESULT DemoApp::OnRender()
{
	HRESULT hr = S_OK;

	hr = CreateDeviceResources();

	if (SUCCEEDED(hr))
	{
		//BeginDraw -> 그리기 -> EndDraw
		m_pRenderTarget->BeginDraw();
		m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
		m_pRenderTarget->Clear(D2D1::ColorF(D2D1::ColorF::White));

		D2D1_SIZE_F rtSize = m_pRenderTarget->GetSize();

		// 격자 배경을 그림.
		int width = static_cast<int>(rtSize.width);
		int height = static_cast<int>(rtSize.height);
		for (int x = 0; x < width; x += 10)
		{
			m_pRenderTarget->DrawLine(
				D2D1::Point2F(static_cast<FLOAT>(x), 0.0f),
				D2D1::Point2F(static_cast<FLOAT>(x), rtSize.height),
				m_pLightSlateGrayBrush, 0.5f
			);
		}

		for (int y = 0; y < height; y += 10)
		{
			m_pRenderTarget->DrawLine(
				D2D1::Point2F(0.0f, static_cast<FLOAT>(y)),
				D2D1::Point2F(rtSize.width, static_cast<FLOAT>(y)),
				m_pLightSlateGrayBrush, 0.5f
			);
		}
		//시작 사격형
		D2D1_RECT_F rectangle_tmp = D2D1::RectF(start_box.left, start_box.top, start_box.right, start_box.bottom);
		//둥글게 하기
		D2D1_ROUNDED_RECT start_rectangle = D2D1::RoundedRect(rectangle_tmp, 20, 10);
		
		////좌상단 마우스 위치 표기하기
		m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
		WCHAR mouse_text[100];
		swprintf_s(mouse_text, L"201701531 김기태\nX좌표 : %.2f \nY좌표 : %.2f \n회전각도 : %.2f\n박스 크기 : %.2f\n박스개수 : %d"
			,mouse_point.x, mouse_point.y, Angle, Size, people_vector.size());
		//swprintf_s(mouse_text, L"size: %.2f\nsize2:%.2f", test1, test2);

		m_pRenderTarget->DrawText(
			mouse_text,
			wcslen(mouse_text),
			m_pTextFormat,
			D2D1::RectF(10.0f, 10.0f, 200.0f, 200.0f),
			m_pTextBrush
		); //좌상단에 쓰여지는 정보 쓰기
		// 채워진 사각형을 그림. (투명도 0.5)
		m_pTransparentBox->SetOpacity(0.5f);
		m_pRenderTarget->FillRoundedRectangle(&start_rectangle, m_pTransparentBox);

		//상자그리기
		draw_box();
		if (isdrag && isinsert) {
			m_pCornflowerBlueBrush->SetOpacity(0.5f);
			draw_next_box();
			draw_moving_Rectangle();
		}
		if (isdrag && isdelete) {
			draw_next_box();
			draw_moving_Rectangle();
		}
		//그린것을 표현함
		hr = m_pRenderTarget->EndDraw();
	}
	if (hr == D2DERR_RECREATE_TARGET)
	{
		hr = S_OK;
		DiscardDeviceResources();
	}
	return hr;
}

// 응용 프로그램에서 WM_SIZE 메시지가 발생되면 이 함수가 렌더타겟의 크기를 다시 설정함.
void DemoApp::OnResize(UINT width, UINT height)
{
	if (m_pRenderTarget)
	{
		// 참고: 아래의 함수는 실행 시에 실패할 수도 있지만, 여기에서 실행 에러를 체크하지 않고 넘어가도 됨. 다음번 EndDraw 함수가 호출될 때에 에러가 확인될 것이므로 그때 처리하면 됨.
		m_pRenderTarget->Resize(D2D1::SizeU(width, height));
	}
}

// 윈도우 메시지를 처리.
LRESULT CALLBACK DemoApp::WndProc(HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam)
{
	LRESULT result = 0;

	if (message == WM_CREATE)
	{
		LPCREATESTRUCT pcs = (LPCREATESTRUCT)lParam;
		DemoApp* pDemoApp = (DemoApp*)pcs->lpCreateParams;

		SetWindowLongPtrW(hwnd, GWLP_USERDATA, PtrToUlong(pDemoApp));

		result = 1;
	}
	else
	{
		DemoApp* pDemoApp = reinterpret_cast<DemoApp*>(static_cast<LONG_PTR>(GetWindowLongPtrW(hwnd, GWLP_USERDATA)));

		bool wasHandled = false;

		if (pDemoApp)
		{
			switch (message)
			{
			case WM_SIZE:
			{
				UINT width = LOWORD(lParam);
				UINT height = HIWORD(lParam);
				pDemoApp->OnResize(width, height);
			}
			result = 0;
			wasHandled = true;
			break;

			case WM_DISPLAYCHANGE:
			{
				InvalidateRect(hwnd, NULL, FALSE);
			}
			result = 0;
			wasHandled = true;
			break;

			case WM_PAINT:
			{
				pDemoApp->OnRender();
				ValidateRect(hwnd, NULL);
			}
			result = 0;
			wasHandled = true;
			break;

			//마우스 움직일 때
			case WM_MOUSEMOVE:
			{
				//마우스 정보 전달
				mouse_point.x = LOWORD(lParam);
				mouse_point.y = HIWORD(lParam);

				//해당 위치에 도달하면 stack에 넣는다.
				float square_t = stack_box.top - (people_box_height + padding) * people_vector.size();
				float square_b = stack_box.bottom - (people_box_height + padding) * people_vector.size();

				//만약에 하단의 그려질 위치에 도달한다면 학생을 추가한다.
				if (isdrag == true && isinsert == true) {
					if (LOWORD(lParam) >= stack_box.left && LOWORD(lParam) <= stack_box.right
						&& HIWORD(lParam) >= square_t && HIWORD(lParam) <= square_b) {
						if (people_vector.size() < 10) {
							isinsert_inner = true;
						}
						else {
						}
					}
					else {
						isinsert_inner = false;
					}
				}
				//상단의 가상상자에 도달하면 학생 정보를 지운다.
				if (isdrag == true && isdelete == true) {
					if (LOWORD(lParam) >= start_box.left && LOWORD(lParam) <= start_box.right
						&& HIWORD(lParam) >= start_box.top && HIWORD(lParam) <= start_box.bottom) {
						isdelete_inner = true;
					}
					else {
						isdelete_inner = false;
					}
				}
				InvalidateRect(hwnd, NULL, false);
				return 0;
			}
			//마우스 왼쪽 버튼이 올라갈 때 (즉 드래그 하다가 끊김)
			case WM_LBUTTONUP: // 만약 드래그가 끊겼으면 
			{
				if (isdrag && isinsert && isinsert_inner) {
					isinsert_inner = false;
					//create_student();
					insert_student();
					std::sort(people_vector.begin(), people_vector.end(), compare);
				}
				else if (isdrag && isdelete) {
					if (!isdelete_inner) {
						//삭제모드이지만, 상단 상자에 들어가지 못한 경우
						people_vector.push_back(student_tmp);
					}
					isdelete_inner = false;
				}
				isdrag = false; isinsert = false; isdelete = false;
				Angle = 0.0f;
				Size = 0.0f;
				InvalidateRect(hwnd, NULL, false);
				break;
			}
			//왼쪽 마우스 클릭 시
			case WM_LBUTTONDOWN:
			{
				//삽입모드
				if (LOWORD(lParam) >= start_box.left && LOWORD(lParam) <= start_box.right
					&& HIWORD(lParam) >= start_box.top && HIWORD(lParam) <= start_box.bottom) {
					if (people_vector.size() < 10) {
						isdrag = true; isinsert = true;
						last_mouse_point.x = LOWORD(lParam);
						last_mouse_point.y = HIWORD(lParam);

						//학생정보를 하나 만든다.
						create_student();
					}
					else {
						//메세지 박스 여러번 띄우기 방지
						if (!locked) {
							locked = true;
							if (MessageBox(0, L"최대 10개까지 채울 수 있습니다.", L"최대 개수", MB_ICONEXCLAMATION) == IDOK) {						
								locked = false;
							}
						}
					}
				}
				//삭제모드
				nextbox t = present_coordinate(people_vector.size());
				if (LOWORD(lParam) >= t.left && LOWORD(lParam) <= t.right
					&& HIWORD(lParam) >= t.top + people_box_height && HIWORD(lParam) <= t.bottom + people_box_height && !people_vector.empty()) {
					isdrag = true; isdelete = true;
					delete_student();
				}
				InvalidateRect(hwnd, NULL, false);
				break;
			}
			case WM_DESTROY:
			{
				PostQuitMessage(0);
			}
			result = 1;
			wasHandled = true;
			break;
			}
		}
		if (!wasHandled)
		{
			result = DefWindowProc(hwnd, message, wParam, lParam);
		}
	}
	return result;
}
