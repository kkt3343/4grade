#include "201701531_kkt_HW2.h"

// �������� ����
//�޼��� �ڽ� ������ ���� ���� �ڵ�
boolean locked = false;

//�������� ũ�� ����
int windows_width = 600;
int windows_height = 900;

float start_width = windows_width / 2;
float start_height = 100;

/*���� ���콺 Ȯ�� ����Ʈ*/
D2D_POINT_2F mouse_point;
D2D_POINT_2F last_mouse_point;

//���� �� ������ ���
float Angle = 0;
float Size = 0;

//���콺�� ���� Ȯ��
boolean isdrag;
boolean isinsert;
boolean isdelete;

boolean isinsert_inner;
boolean isdelete_inner;

//���� �簢���� ��ǥ (ȸ�� ����)
//���ۻ����� ũ�� ����
//�׻� ���̿� �ʺ�� 1:2 �̿��� �Ѵ�.
int start_box_height = 50;
int start_box_width = start_box_height * 2;
struct startbox {
	float left = windows_width / 2 - start_box_width / 2; // 250
	float top = 100 - start_box_height/2; //75
	float right = windows_width / 2 + start_box_width / 2; // 350
	float bottom = 100 + start_box_height / 2; //125
};
startbox start_box;

//�л������� ũ�� ����
//�׻� ���̿� �ʺ�� 1:4 �̿��� �Ѵ�.
int people_box_height = 50;
int people_box_width = people_box_height * 4;
int padding = 5;

//�ϴ��� �������� ������ â�� -100

struct stackbox {
	float left = windows_width / 2 - people_box_width / 2;
	float top = windows_height - 100 - people_box_height;
	float right = windows_width / 2 + people_box_width / 2;
	float bottom = windows_height - 100;
};
stackbox stack_box;

//�ڽ��� �ֻ���� ��ġ�� ����Ѵ�.
struct nextbox {
	float left = windows_width / 2 - people_box_width / 2;
	float top;
	float right = windows_width / 2 + people_box_width / 2;
	float bottom;
};

nextbox present_coordinate(int stackSize) {
	//�л� ������ ������� ��ǥ�� ���Ѵ�.
	nextbox tmp;
	tmp.top = stack_box.top - (people_box_height + padding) * stackSize;
	tmp.bottom = stack_box.bottom - (people_box_height + padding) * stackSize;
	return tmp;
}

student student_tmp;

// ���� ���α׷��� ������ �Լ�
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

// ������. ��� �������� �ʱ�ȭ��.
DemoApp::DemoApp():
	m_hwnd(NULL),
	m_pDirect2dFactory(NULL),
	m_pRenderTarget(NULL),
	m_pLightSlateGrayBrush(NULL),
	m_pCornflowerBlueBrush(NULL),

	//�׵θ�
	m_border_brush(NULL),

	//�귯��
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

	//����ڽ�
	m_pTransparentBox(NULL),

	m_pTextBrush(NULL),

	//�۾�
	m_pDWriteFactory(NULL),
	m_pTextFormat(NULL)
{
}

// �Ҹ���. ���� ���α׷��� �ڿ��� �ݳ���.
DemoApp::~DemoApp()
{
	//���� �Լ����� �����
	DiscardDeviceResources();
	SAFE_RELEASE(m_pDirect2dFactory);

	//�۾�
	SAFE_RELEASE(m_pTextBrush);
	SAFE_RELEASE(m_pDWriteFactory);
	SAFE_RELEASE(m_pTextFormat);
}

// ���� ���α׷��� �����츦 ������. ��ġ ������ �ڿ��� ������.
HRESULT DemoApp::Initialize(HINSTANCE hInstance)
{
	HRESULT hr;

	// ��ġ ������ �ڿ��� ������.
	hr = CreateDeviceIndependentResources();

	if (SUCCEEDED(hr))
	{
		// ������ Ŭ������ �����..
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

		// �����츦 ������. (Title��)
		m_hwnd = CreateWindow(
			L"D2DDemoApp", L"201701531_�����_HW2",
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

// ��ġ ������ �ڿ����� ������. �̵� �ڿ��� ������ ���� ���α׷��� ����Ǳ� ������ ��ȿ��.
HRESULT DemoApp::CreateDeviceIndependentResources()
{
	static const WCHAR msc_fontName[] = L"Verdana";
	static const FLOAT msc_fontSize = 50;

	HRESULT hr = S_OK;

	// D2D ���丮�� ������.
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


// ��ġ ������ �ڿ����� ������. ��ġ�� �ҽǵǴ� ��쿡�� �̵� �ڿ��� �ٽ� �����ؾ� ��.
HRESULT DemoApp::CreateDeviceResources()
{
	HRESULT hr = S_OK;
	// Create a Direct2D factory.
	
	if (!m_pRenderTarget)
	{
		RECT rc;
		GetClientRect(m_hwnd, &rc);

		D2D1_SIZE_U size = D2D1::SizeU(rc.right - rc.left, rc.bottom - rc.top);

		// D2D ����Ÿ���� ������.
		hr = m_pDirect2dFactory->CreateHwndRenderTarget(D2D1::RenderTargetProperties(), D2D1::HwndRenderTargetProperties(m_hwnd, size), &m_pRenderTarget);

		if (SUCCEEDED(hr))
		{
			// ȸ�� ���� ������. //���ڸ� �׸��� �뵵
			hr = m_pRenderTarget->CreateSolidColorBrush(D2D1::ColorF(D2D1::ColorF::LightGray), &m_pLightSlateGrayBrush);
		}
		if (SUCCEEDED(hr))
		{
			// �������� ���� ��ġ ��(����)
			hr = m_pRenderTarget->CreateSolidColorBrush(D2D1::ColorF(D2D1::ColorF::CornflowerBlue), &m_pCornflowerBlueBrush);
		}
		if (SUCCEEDED(hr))
		{
			// ����� ���� �� �巡�� �� �� �����̴� ���ڸ� ���� ��
			hr = m_pRenderTarget->CreateSolidColorBrush(D2D1::ColorF(D2D1::ColorF::Gray), &m_pTransparentBox);
		}
		if (SUCCEEDED(hr))
		{
			// �׵θ�
			hr = m_pRenderTarget->CreateSolidColorBrush(D2D1::ColorF(D2D1::ColorF::Black), &m_border_brush);
		}
		//�귯�� ����
		if (SUCCEEDED(hr))
		{
			// �ڽ� �� ����
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
			//�۾�
			// Create a solid color brush for writing text.
			hr = m_pRenderTarget->CreateSolidColorBrush(
				D2D1::ColorF(D2D1::ColorF::Black),
				&m_pTextBrush
			);
		}
	}
	return hr;
}

// ��ġ ������ �ڿ����� �ݳ���. ��ġ�� �ҽǵǸ� �̵� �ڿ��� �ٽ� �����ؾ� ��.
void DemoApp::DiscardDeviceResources()
{
	SAFE_RELEASE(m_pRenderTarget);
	SAFE_RELEASE(m_pLightSlateGrayBrush);
	SAFE_RELEASE(m_pCornflowerBlueBrush);
	//�׵θ�
	SAFE_RELEASE(m_border_brush);

	//�귯��
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

	//����ڽ�
	SAFE_RELEASE(m_pTransparentBox);
}

// ���� ������ �޽��� ������ ������.
void DemoApp::RunMessageLoop()
{
	MSG msg;

	while (GetMessage(&msg, NULL, 0, 0))
	{
		TranslateMessage(&msg);
		DispatchMessage(&msg);
	}
}

//�л�
student people;
std::vector<student> people_vector;
void create_student()
{
	int family_random = rand() % (sizeof(familyNames) / sizeof(std::string)); //0 ~ 17
	int first_random1 = rand() % (sizeof(firstNames) / sizeof(std::string)); //0 ~ 21
	int first_random2 = rand() % (sizeof(firstNames) / sizeof(std::string)); //0 ~ 21

	//�̸�����
	student_tmp.name = familyNames[family_random] + firstNames[first_random1] + firstNames[first_random2];
	//��������
	student_tmp.score = rand() % 61 + 30;

	////�߰�
	//people_vector.push_back(people);
}

void insert_student()
{
	//�߰�
	people_vector.push_back(student_tmp);
}

void delete_student()
{
	//�켱 �� �ڸ� ����� �صΰ� ���࿡ �巡�װ� �߰��� ����� �����ϰ� �׷��� ������ ������.
	student_tmp = people_vector.back();
	people_vector.pop_back();
}

//string -> wstring���� ��ȯ�� ���� �Լ�
std::wstring strconv(const std::string& _src)
{
	USES_CONVERSION;
	return std::wstring(A2W(_src.c_str()));

};

//�л����� ����
void DemoApp::draw_box()
{
	float square_t = stack_box.top;
	float square_b = stack_box.bottom;

	D2D1_RECT_F rectangle_tmp;
	for (int i = 0; i < people_vector.size(); i++) {
		rectangle_tmp = D2D1::RectF(stack_box.left, square_t - (people_box_height + padding) * i, stack_box.right, square_b - (people_box_height + padding) * i);
		if (i == 0) {
			//�簢�� ���� ��ĥ
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
		//�簢�� �׵θ� ��ĥ
		m_pRenderTarget->DrawRoundedRectangle(D2D1::RoundedRect(rectangle_tmp, 10, 10), m_border_brush, 2.0f);
		//�л� ������ ǥ��
		m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
		WCHAR mouse_text[100];
		
		std::string tmp = people_vector[i].name + "\t" + std::to_string(people_vector[i].score) + "��";
		std::wstring wtmp = strconv(tmp);
		const WCHAR* t = wtmp.c_str();

		swprintf_s(mouse_text, t);
		m_pRenderTarget->DrawText(
			mouse_text,
			wcslen(mouse_text),
			m_pTextFormat,
			D2D1::RectF(stack_box.left + 30, square_t - (people_box_height + padding) * i + 10, stack_box.right - 30, square_b - (people_box_height + padding) * i),
			m_pTextBrush
		); //�»�ܿ� �������� ���� ����
	}
}

//�������� ���� ��ġ
void DemoApp::draw_next_box()
{
	nextbox n = present_coordinate(people_vector.size());

	D2D1_RECT_F rectangle_tmp = D2D1::RectF(n.left,n.top,n.right,n.bottom);
	D2D1_ROUNDED_RECT draw_box_input = D2D1::RoundedRect(rectangle_tmp, 10, 10);
	m_pRenderTarget->FillRoundedRectangle(&draw_box_input, m_pCornflowerBlueBrush);
}

//�巡���ϸ鼭 �׸� ���ڸ� ����� �Լ�
void DemoApp::draw_moving_Rectangle() {

	//���� �����
	D2D1_RECT_F rectangle_tmp = D2D1::RectF(start_box.left, start_box.top, start_box.right, start_box.bottom);
	D2D1_ROUNDED_RECT draw_box_input = D2D1::RoundedRect(rectangle_tmp, 10, 10);

	nextbox t = present_coordinate(people_vector.size() + 1);
	float tt = (t.top - 20);

	//�����
	//���������� ũ�⸦ Ű���, ȸ���� ��Ų ���� �̵� �Ѵ�.
	//�׷��� scale �� rotation�� ���ڸ� ����� �������� ó���Ѵ�.

	//1�� ~ 2��
	Size = (mouse_point.y - last_mouse_point.y) / (tt) + 1.0f;
	Angle = ((mouse_point.y - last_mouse_point.y) / (tt)) * 360.0f;
	
	D2D1_MATRIX_3X2_F scale = D2D1::Matrix3x2F::Scale(D2D1::Size(Size, 1.0f), D2D1::Point2F((start_box.left + start_box.right)/2 , (start_box.top + start_box.bottom) / 2));
	D2D1_MATRIX_3X2_F rotation = D2D1::Matrix3x2F::Rotation(Angle, D2D1::Point2F((start_box.left + start_box.right) / 2, (start_box.top + start_box.bottom) / 2));
	D2D1_MATRIX_3X2_F translation = D2D1::Matrix3x2F::Translation((mouse_point.x - last_mouse_point.x), (mouse_point.y - last_mouse_point.y));
	
	m_pRenderTarget->SetTransform(scale * rotation * translation); // scale->rotation->translation ��
	m_pRenderTarget->DrawRoundedRectangle(draw_box_input, m_border_brush, 1.0f);

	//
	int i = people_vector.size();
	if (i == 0) {
		//�簢�� ���� ��ĥ
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

	//���������� ǥ��
	m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
	WCHAR mouse_text[100];
	std::string tmp = student_tmp.name + "\t" + std::to_string(student_tmp.score) + "��";
	std::wstring wtmp = strconv(tmp);
	const WCHAR* tp = wtmp.c_str();

	//�۾��� �̵��� ��Ű��, scale�� ���� �ʴ´�.
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

// Ŭ���̾�Ʈ ������ ȭ���� �׸�.
// ����: �� �Լ��� ����Ǵ� ���ȿ� ��ġ�� �ҽǵǸ� ��ġ ������ �ڿ����� �ݳ���. �� ���� ȣ�� �ÿ� �� �ڿ����� �ٽ� ������.
HRESULT DemoApp::OnRender()
{
	HRESULT hr = S_OK;

	hr = CreateDeviceResources();

	if (SUCCEEDED(hr))
	{
		//BeginDraw -> �׸��� -> EndDraw
		m_pRenderTarget->BeginDraw();
		m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
		m_pRenderTarget->Clear(D2D1::ColorF(D2D1::ColorF::White));

		D2D1_SIZE_F rtSize = m_pRenderTarget->GetSize();

		// ���� ����� �׸�.
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
		//���� �����
		D2D1_RECT_F rectangle_tmp = D2D1::RectF(start_box.left, start_box.top, start_box.right, start_box.bottom);
		//�ձ۰� �ϱ�
		D2D1_ROUNDED_RECT start_rectangle = D2D1::RoundedRect(rectangle_tmp, 20, 10);
		
		////�»�� ���콺 ��ġ ǥ���ϱ�
		m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
		WCHAR mouse_text[100];
		swprintf_s(mouse_text, L"201701531 �����\nX��ǥ : %.2f \nY��ǥ : %.2f \nȸ������ : %.2f\n�ڽ� ũ�� : %.2f\n�ڽ����� : %d"
			,mouse_point.x, mouse_point.y, Angle, Size, people_vector.size());
		//swprintf_s(mouse_text, L"size: %.2f\nsize2:%.2f", test1, test2);

		m_pRenderTarget->DrawText(
			mouse_text,
			wcslen(mouse_text),
			m_pTextFormat,
			D2D1::RectF(10.0f, 10.0f, 200.0f, 200.0f),
			m_pTextBrush
		); //�»�ܿ� �������� ���� ����
		// ä���� �簢���� �׸�. (���� 0.5)
		m_pTransparentBox->SetOpacity(0.5f);
		m_pRenderTarget->FillRoundedRectangle(&start_rectangle, m_pTransparentBox);

		//���ڱ׸���
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
		//�׸����� ǥ����
		hr = m_pRenderTarget->EndDraw();
	}
	if (hr == D2DERR_RECREATE_TARGET)
	{
		hr = S_OK;
		DiscardDeviceResources();
	}
	return hr;
}

// ���� ���α׷����� WM_SIZE �޽����� �߻��Ǹ� �� �Լ��� ����Ÿ���� ũ�⸦ �ٽ� ������.
void DemoApp::OnResize(UINT width, UINT height)
{
	if (m_pRenderTarget)
	{
		// ����: �Ʒ��� �Լ��� ���� �ÿ� ������ ���� ������, ���⿡�� ���� ������ üũ���� �ʰ� �Ѿ�� ��. ������ EndDraw �Լ��� ȣ��� ���� ������ Ȯ�ε� ���̹Ƿ� �׶� ó���ϸ� ��.
		m_pRenderTarget->Resize(D2D1::SizeU(width, height));
	}
}

// ������ �޽����� ó��.
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

			//���콺 ������ ��
			case WM_MOUSEMOVE:
			{
				//���콺 ���� ����
				mouse_point.x = LOWORD(lParam);
				mouse_point.y = HIWORD(lParam);

				//�ش� ��ġ�� �����ϸ� stack�� �ִ´�.
				float square_t = stack_box.top - (people_box_height + padding) * people_vector.size();
				float square_b = stack_box.bottom - (people_box_height + padding) * people_vector.size();

				//���࿡ �ϴ��� �׷��� ��ġ�� �����Ѵٸ� �л��� �߰��Ѵ�.
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
				//����� ������ڿ� �����ϸ� �л� ������ �����.
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
			//���콺 ���� ��ư�� �ö� �� (�� �巡�� �ϴٰ� ����)
			case WM_LBUTTONUP: // ���� �巡�װ� �������� 
			{
				if (isdrag && isinsert && isinsert_inner) {
					isinsert_inner = false;
					//create_student();
					insert_student();
					std::sort(people_vector.begin(), people_vector.end(), compare);
				}
				else if (isdrag && isdelete) {
					if (!isdelete_inner) {
						//�������������, ��� ���ڿ� ���� ���� ���
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
			//���� ���콺 Ŭ�� ��
			case WM_LBUTTONDOWN:
			{
				//���Ը��
				if (LOWORD(lParam) >= start_box.left && LOWORD(lParam) <= start_box.right
					&& HIWORD(lParam) >= start_box.top && HIWORD(lParam) <= start_box.bottom) {
					if (people_vector.size() < 10) {
						isdrag = true; isinsert = true;
						last_mouse_point.x = LOWORD(lParam);
						last_mouse_point.y = HIWORD(lParam);

						//�л������� �ϳ� �����.
						create_student();
					}
					else {
						//�޼��� �ڽ� ������ ���� ����
						if (!locked) {
							locked = true;
							if (MessageBox(0, L"�ִ� 10������ ä�� �� �ֽ��ϴ�.", L"�ִ� ����", MB_ICONEXCLAMATION) == IDOK) {						
								locked = false;
							}
						}
					}
				}
				//�������
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
