#include "201701531_�����_HW4.h"

// �������� ����
//ĳ���� ��ǥ ����
D2D1_POINT_2F mycharacter_point = { 800, 400 };
D2D1_SIZE_U mycharacter_size;
int score = 0;
int move = 0;
bool moveright = true;
bool isshow = false;

//������ �ֹ� �Ϸᶧ���� �ҷ����� �ʱ�
bool islock = false;
//�ֹ�����
int makepizza = 0;
int orderpizza = 0;

bool orderfinish = false;

//HW4 ���� ũ��
bool sound_true = true;
int sound_volume = 70;

//�������� ũ�� ����
int windows_width = 1500;
int windows_height = 900;

//ĳ���� ��Ʈ�� �ӽ�����
ID2D1BitmapBrush* character_tmp;

//������� ���� üũ
int pizza_ingredient[7] = { 0 };

double top_box_x_point[] = {
	120,
	240,
	360,
	480,
	600,
	720,
	840,
	960,
	1080
};

double bottom_box_x_point[] ={
	100,
	200,
	300,
	400,
	500,
	600,
	700,
	800,
	900,
	1100};


//HW4 AI
FollowCat* followcat = new FollowCat();
PizzaCat* pizzacat = new PizzaCat();
float guest_timer = 0;
bool guest_lock = false;

bool sound_one_time = false;
bool gameclear = false;
bool gamefail = false;

//���� ������ �ð� ����
int pizza_per_time = 10;
//���� Ŭ����, ���� Ƚ�� ���ϱ�
int game_clear_count = 20;
int game_fail_count = 5;

bool voice_lock = false;

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
DemoApp::DemoApp() :
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
	m_white(NULL),

	//����ڽ�
	m_pTransparentBox(NULL),

	//�ؽ�Ʈ
	m_pTextBrush_white(NULL),
	m_pTextBrush_black(NULL),
	m_pDWriteFactory(NULL),
	m_pTextFormat(NULL),
	m_pTextFormat_bigsize(NULL),

	//���
	m_pWICFactory(NULL),
	m_pBitmap(NULL),
	m_pBitmap_desk(NULL),
	m_ptables1(NULL),
	m_ptables2(NULL),
	m_pBitmap_gameclear(NULL),
	m_pBitmap_gameover(NULL),

	//ĳ����
	m_pMyCharacter(NULL),
	m_pOtherCharacter1(NULL),
	m_pOtherCharacter2(NULL),
	m_pOtherCharacter3(NULL),
	m_pOtherCharacter4(NULL),
	m_pOtherCharacter5(NULL),
	m_pOtherCharacter6(NULL),

	//��ǳ��
	m_pspeech_bubble(NULL),
	m_pspeech_bubble_brush(NULL),

	//���� ����
	m_pizza_dow(NULL),
	m_pizza_cheese(NULL),
	m_pizza_mushroom(NULL),
	m_pizza_onion(NULL),
	m_pizza_olive(NULL),
	m_pizza_pepperoni(NULL),
	m_pizza_pmang(NULL),

	//Geometry
	top_PathGeometry(NULL),
	bottom_PathGeometry(NULL),
	guest_PathGeometry(NULL),
	guest_PathGeometry_back(NULL),
	child_PathGeometry(NULL),

	//Brush
	m_pOtherCharacter1_brush(NULL),
	m_pOtherCharacter2_brush(NULL),
	m_pOtherCharacter3_brush(NULL),
	m_pOtherCharacter4_brush(NULL),
	m_pOtherCharacter5_brush(NULL),
	m_pOtherCharacter6_brush(NULL),

	m_pizza_dow_brush(NULL),
	m_pizza_cheese_brush(NULL),
	m_pizza_mushroom_brush(NULL),
	m_pizza_onion_brush(NULL),
	m_pizza_olive_brush(NULL),
	m_pizza_pepperoni_brush(NULL),
	m_pizza_pmang_brush(NULL),

	m_pRectGeo1(NULL)
{
}

// �Ҹ���. ���� ���α׷��� �ڿ��� �ݳ���.
DemoApp::~DemoApp()
{
	//���� �Լ����� �����
	DiscardDeviceResources();
	SAFE_RELEASE(m_pDirect2dFactory);

	//�ؽ�Ʈ
	SAFE_RELEASE(m_pTextBrush_white);
	SAFE_RELEASE(m_pTextBrush_black);
	SAFE_RELEASE(m_pDWriteFactory);
	SAFE_RELEASE(m_pTextFormat);
	SAFE_RELEASE(m_pTextFormat_bigsize);

	//���
	SAFE_RELEASE(m_pWICFactory);
	SAFE_RELEASE(m_pBitmap);
	SAFE_RELEASE(m_pBitmap_desk);
	SAFE_RELEASE(m_ptables1);
	SAFE_RELEASE(m_ptables2);
	SAFE_RELEASE(m_pBitmap_gameclear);
	SAFE_RELEASE(m_pBitmap_gameover);

	//ĳ����
	SAFE_RELEASE(m_pMyCharacter);
	SAFE_RELEASE(m_pOtherCharacter1);
	SAFE_RELEASE(m_pOtherCharacter2);
	SAFE_RELEASE(m_pOtherCharacter3);
	SAFE_RELEASE(m_pOtherCharacter4);
	SAFE_RELEASE(m_pOtherCharacter5);
	SAFE_RELEASE(m_pOtherCharacter6);

	//��ǳ��
	SAFE_RELEASE(m_pspeech_bubble);
	
	//���� ����
	SAFE_RELEASE(m_pizza_dow);
	SAFE_RELEASE(m_pizza_cheese);
	SAFE_RELEASE(m_pizza_mushroom);
	SAFE_RELEASE(m_pizza_onion);
	SAFE_RELEASE(m_pizza_olive);
	SAFE_RELEASE(m_pizza_pepperoni);
	SAFE_RELEASE(m_pizza_pmang);

	//Geometry
	SAFE_RELEASE(top_PathGeometry);
	SAFE_RELEASE(bottom_PathGeometry);
	SAFE_RELEASE(guest_PathGeometry);
	SAFE_RELEASE(guest_PathGeometry_back)
	SAFE_RELEASE(child_PathGeometry);

	SAFE_RELEASE(m_pRectGeo1);
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
			L"D2DDemoApp", L"201701531_�����_HW4",
			WS_OVERLAPPEDWINDOW, CW_USEDEFAULT, CW_USEDEFAULT,
			windows_width, windows_height, NULL, NULL, hInstance, this
		);
		hr = m_hwnd ? S_OK : E_FAIL;
		if (SUCCEEDED(hr))
		{
			ShowWindow(m_hwnd, SW_SHOWNORMAL);
			UpdateWindow(m_hwnd);
		}
		QueryPerformanceFrequency(&m_nFrequency);
		QueryPerformanceCounter(&m_nPrevTime);
	}

	//�մ� ���� ��� ����
	if (SUCCEEDED(hr))
	{
		float length = 0;
		hr = guest_PathGeometry->ComputeLength(NULL, &length);

		if (SUCCEEDED(hr))
		{
			m_Animation.SetStart(0); //start at beginning of path
			m_Animation.SetEnd(length); //length at end of path
			m_Animation.SetDuration(10.0f); //seconds

			ShowWindow(m_hwnd, SW_SHOWNORMAL);
			UpdateWindow(m_hwnd);
		}
	}

	//�մ�_������ ���� ��� ����
	if (SUCCEEDED(hr))
	{
		float length = 0;
		hr = guest_PathGeometry_back->ComputeLength(NULL, &length);

		if (SUCCEEDED(hr))
		{
			m_Animation1.SetStart(0); //start at beginning of path
			m_Animation1.SetEnd(length); //length at end of path
			m_Animation1.SetDuration(5.0f); //seconds

			ShowWindow(m_hwnd, SW_SHOWNORMAL);
			UpdateWindow(m_hwnd);
		}
	}

	//���� ���� ��� ����
	if (SUCCEEDED(hr))
	{
		float length = 0;
		hr = child_PathGeometry->ComputeLength(NULL, &length);

		if (SUCCEEDED(hr))
		{
			m_Animation2.SetStart(0); //start at beginning of path
			m_Animation2.SetEnd(length); //length at end of path
			m_Animation2.SetDuration(10.0f); //seconds

			ShowWindow(m_hwnd, SW_SHOWNORMAL);
			UpdateWindow(m_hwnd);
		}
	}

	//HW4 ����
	if (SUCCEEDED(hr))
	{
		// sound
		initSound(m_hwnd);
		soundManager->pDSBPrimary->SetVolume(DSVOLUME_TO_DB(sound_volume));
		soundManager->play(0, TRUE);
	}
	return hr;
}

// ��ġ ������ �ڿ����� ������. �̵� �ڿ��� ������ ���� ���α׷��� ����Ǳ� ������ ��ȿ��.
HRESULT DemoApp::CreateDeviceIndependentResources()
{
	static const WCHAR msc_fontName[] = L"Verdana";
	static const FLOAT msc_fontSize = 50;

	HRESULT hr = S_OK;
	ID2D1GeometrySink* pSink = NULL;

	//HW3
	// WIC ���丮�� ������.
	hr = CoCreateInstance(CLSID_WICImagingFactory, NULL, CLSCTX_INPROC_SERVER, IID_PPV_ARGS(&m_pWICFactory));
	// D2D ���丮�� ������.
	if (SUCCEEDED(hr)) {
		hr = D2D1CreateFactory(D2D1_FACTORY_TYPE_SINGLE_THREADED, &m_pDirect2dFactory);
	}

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

		hr = m_pDWriteFactory->CreateTextFormat(
			L"Verdana",     // The font family name.
			NULL,           // The font collection (NULL sets it to use the system font collection).
			DWRITE_FONT_WEIGHT_REGULAR,
			DWRITE_FONT_STYLE_NORMAL,
			DWRITE_FONT_STRETCH_NORMAL,
			21.0f,
			L"ko-kr",
			&m_pTextFormat_bigsize
		);
	}

	//��������ũ
	if (SUCCEEDED(hr))
	{
		hr = m_pDirect2dFactory->CreateRectangleGeometry(D2D1::RectF(0, 0, 1000, 1000), &m_pRectGeo1);
	}

	//���� ��� ���� �׸���
	if (SUCCEEDED(hr))
	{
		hr = m_pDirect2dFactory->CreatePathGeometry(&child_PathGeometry);

		if (SUCCEEDED(hr))
		{
			hr = child_PathGeometry->Open(&pSink);

			if (SUCCEEDED(hr))
			{
				D2D1_POINT_2F currentLocation = { 200.0f, 650.0f };

				pSink->BeginFigure(currentLocation, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddBezier(D2D1::BezierSegment(D2D1::Point2F(400, 700), D2D1::Point2F(600, 630), D2D1::Point2F(800, 650)));
				pSink->AddBezier(D2D1::BezierSegment(D2D1::Point2F(800, 650), D2D1::Point2F(600, 700), D2D1::Point2F(200, 650)));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				hr = pSink->Close();
			}
			SAFE_RELEASE(pSink);
		}
	}

	//����� ���� ���� �׸���
	if (SUCCEEDED(hr))
	{
		hr = m_pDirect2dFactory->CreatePathGeometry(&top_PathGeometry);

		if (SUCCEEDED(hr))
		{
			hr = top_PathGeometry->Open(&pSink);

			if (SUCCEEDED(hr))
			{
				//���� ���
				D2D1_POINT_2F currentLocation = { 240.0f, 10.0f };
				pSink->BeginFigure(currentLocation, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(1080, 10));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				//���� �ߴ�
				D2D1_POINT_2F currentLocation2 = { 240.0f, 110.0f };
				pSink->BeginFigure(currentLocation2, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(1080, 110));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				//���� �ϴ�
				D2D1_POINT_2F currentLocation3 = { 240.0f, 150.0f };
				pSink->BeginFigure(currentLocation3, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(1080, 150));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				//���� ��(Left)
				D2D1_POINT_2F side1 = { top_box_x_point[1], 10.0f };
				pSink->BeginFigure(side1, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(top_box_x_point[1], 150));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				//���� ��(Right)
				D2D1_POINT_2F side2 = { top_box_x_point[8], 10.0f };
				pSink->BeginFigure(side2, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(top_box_x_point[8], 150));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				//���� ��(����)
				D2D1_POINT_2F inside1 = { top_box_x_point[2], 10.0f };
				pSink->BeginFigure(inside1, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(top_box_x_point[2], 150));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				D2D1_POINT_2F inside2 = { top_box_x_point[3], 10.0f };
				pSink->BeginFigure(inside2, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(top_box_x_point[3], 150));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				D2D1_POINT_2F inside3 = { top_box_x_point[4], 10.0f };
				pSink->BeginFigure(inside3, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(top_box_x_point[4], 150));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				D2D1_POINT_2F inside4 = { top_box_x_point[5], 10.0f };
				pSink->BeginFigure(inside4, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(top_box_x_point[5], 150));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				D2D1_POINT_2F inside5 = { top_box_x_point[6], 10.0f };
				pSink->BeginFigure(inside5, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(top_box_x_point[6], 150));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				D2D1_POINT_2F inside6 = { top_box_x_point[7], 10.0f };
				pSink->BeginFigure(inside6, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(top_box_x_point[7], 150));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				hr = pSink->Close();
			}
			SAFE_RELEASE(pSink);
		}
	}

	//�ϴ��� ���� ���� �׸���
	if (SUCCEEDED(hr))
	{
		hr = m_pDirect2dFactory->CreatePathGeometry(&bottom_PathGeometry);

		if (SUCCEEDED(hr))
		{
			hr = bottom_PathGeometry->Open(&pSink);

			if (SUCCEEDED(hr))
			{
				//�߰��� �� �׸���
				//���� ���
				D2D1_POINT_2F currentLocation_a1 = { 480.0f, 250.0f };
				pSink->BeginFigure(currentLocation_a1, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(1100, 250));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				D2D1_POINT_2F currentLocation_a2 = { 480.0f, 250.0f };
				pSink->BeginFigure(currentLocation_a2, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(480, 350));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				D2D1_POINT_2F currentLocation_a3 = { 1100.0f, 250.0f };
				pSink->BeginFigure(currentLocation_a3, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(1100, 350));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				//���� ���
				D2D1_POINT_2F currentLocation1 = { 200.0f, 350.0f };
				pSink->BeginFigure(currentLocation1, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(1100, 350));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				//���� �ߴ�
				D2D1_POINT_2F currentLocation2 = { 200.0f, 400.0f };
				pSink->BeginFigure(currentLocation2, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(1100, 400));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				//���� �ϴ�
				D2D1_POINT_2F currentLocation3 = { 200.0f, 600.0f };
				pSink->BeginFigure(currentLocation3, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(1100, 600));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				//���� ��(Left)
				D2D1_POINT_2F side1 = { bottom_box_x_point[1], 350.0f };
				pSink->BeginFigure(side1, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(bottom_box_x_point[1], 600));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				//���� ��(Right)
				D2D1_POINT_2F side2 = { bottom_box_x_point[9], 350.0f };
				pSink->BeginFigure(side2, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F( bottom_box_x_point[9], 600));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				//���� ��(����)
				D2D1_POINT_2F inside1 = { bottom_box_x_point[2], 350.0f };
				pSink->BeginFigure(inside1, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(bottom_box_x_point[2], 600));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				D2D1_POINT_2F inside2 = { bottom_box_x_point[3], 350.0f };
				pSink->BeginFigure(inside2, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F( bottom_box_x_point[3], 600));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				D2D1_POINT_2F inside3 = { bottom_box_x_point[4], 350.0f };
				pSink->BeginFigure(inside3, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F( bottom_box_x_point[4], 600));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				D2D1_POINT_2F inside4 = { bottom_box_x_point[5], 350.0f };
				pSink->BeginFigure(inside4, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(bottom_box_x_point[5], 600));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				D2D1_POINT_2F inside5 = { bottom_box_x_point[6], 350.0f };
				pSink->BeginFigure(inside5, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(bottom_box_x_point[6], 600));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				D2D1_POINT_2F inside6 = { bottom_box_x_point[7], 350.0f };
				pSink->BeginFigure(inside6, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(bottom_box_x_point[7], 600));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				D2D1_POINT_2F inside7 = { bottom_box_x_point[8], 350.0f };
				pSink->BeginFigure(inside7, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddLine(D2D1::Point2F(bottom_box_x_point[8], 600));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);

				hr = pSink->Close();
			}
			SAFE_RELEASE(pSink);
		}
	}

	//�մ��� ��� ���� �׸���
	if (SUCCEEDED(hr))
	{
		hr = m_pDirect2dFactory->CreatePathGeometry(&guest_PathGeometry);

		if (SUCCEEDED(hr))
		{
			hr = guest_PathGeometry->Open(&pSink);

			if (SUCCEEDED(hr))
			{
				D2D1_POINT_2F currentLocation = { 1450.0f, 850.0f };
				pSink->BeginFigure(currentLocation, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddBezier(D2D1::BezierSegment(D2D1::Point2F(1450, 850), D2D1::Point2F(985, 787), D2D1::Point2F(1225, 725)));
				pSink->AddBezier(D2D1::BezierSegment(D2D1::Point2F(1225, 725), D2D1::Point2F(1320, 675), D2D1::Point2F(1050, 640)));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);
				hr = pSink->Close();
			}
			SAFE_RELEASE(pSink);
		}
	}

	//�մ�_�������� ��� ���� �׸���
	if (SUCCEEDED(hr))
	{
		hr = m_pDirect2dFactory->CreatePathGeometry(&guest_PathGeometry_back);

		if (SUCCEEDED(hr))
		{
			hr = guest_PathGeometry_back->Open(&pSink);

			if (SUCCEEDED(hr))
			{
				D2D1_POINT_2F currentLocation = { 1050.0f, 640.0f };
				pSink->BeginFigure(currentLocation, D2D1_FIGURE_BEGIN_HOLLOW);
				pSink->AddBezier(D2D1::BezierSegment(D2D1::Point2F(1050, 640), D2D1::Point2F(1150, 750), D2D1::Point2F(1450, 850)));
				pSink->EndFigure(D2D1_FIGURE_END_OPEN);
				hr = pSink->Close();
			}
			SAFE_RELEASE(pSink);
		}
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

		//�̹����ε�
		if (SUCCEEDED(hr))
		{
			//���
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\pizza_shop.jpg", 1500, 900, &m_pBitmap);
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\pizza_shop_desk.png", 1500, 900, &m_pBitmap_desk);
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\tables1.png", 1500, 900, &m_ptables1);
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\tables2.png", 1500, 900, &m_ptables2);

			//����Ŭ������
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\game_clear.png", 1500, 900, &m_pBitmap_gameclear);
			//���ӿ������
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\game_over.png", 1500, 900, &m_pBitmap_gameover);

			//ĳ����
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\char3.png", 500, 800, &m_pMyCharacter);
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\char1.png", 500, 800, &m_pOtherCharacter1);
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\char2.png", 500, 800, &m_pOtherCharacter2);
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\char4.png", 500, 800, &m_pOtherCharacter3);
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\char5.png", 500, 800, &m_pOtherCharacter4);
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\char6.png", 500, 800, &m_pOtherCharacter5);
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\char7.png", 500, 800, &m_pOtherCharacter6);

			//��ǳ��
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\speech_bubble.png", 800, 600, &m_pspeech_bubble);

			//���� ����
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\pizza\\����.png", 800, 600, &m_pizza_dow);
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\pizza\\ġ��.png", 800, 600, &m_pizza_cheese);
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\pizza\\����.png", 800, 600, &m_pizza_mushroom);
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\pizza\\����.png", 800, 600, &m_pizza_onion);
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\pizza\\�ø���.png", 800, 600, &m_pizza_olive);
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\pizza\\���۷δ�.png", 800, 600, &m_pizza_pepperoni);
			hr = LoadBitmapFromFile(m_pRenderTarget, m_pWICFactory, L".\\img\\pizza\\�Ǹ�.png", 800, 600, &m_pizza_pmang);

		}

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
			hr = m_pRenderTarget->CreateSolidColorBrush(D2D1::ColorF(D2D1::ColorF::White), &m_white);
		}
		if (SUCCEEDED(hr))
		{
			//�۾�
			// Create a solid color brush for writing text.
			hr = m_pRenderTarget->CreateSolidColorBrush(
				D2D1::ColorF(D2D1::ColorF::White),
				&m_pTextBrush_white
			);

			hr = m_pRenderTarget->CreateSolidColorBrush(
				D2D1::ColorF(D2D1::ColorF::Black),
				&m_pTextBrush_black
			);
		}

		//��Ʈ�� �귯�� ����
		if (SUCCEEDED(hr)) {
			D2D1_BITMAP_BRUSH_PROPERTIES propertiesXClampYClamp = D2D1::BitmapBrushProperties(
				D2D1_EXTEND_MODE_CLAMP,
				D2D1_EXTEND_MODE_CLAMP,
				D2D1_BITMAP_INTERPOLATION_MODE_NEAREST_NEIGHBOR
			);
			if (SUCCEEDED(hr))
			{
				hr = m_pRenderTarget->CreateBitmapBrush(
					m_pOtherCharacter1,
					propertiesXClampYClamp,
					&m_pOtherCharacter1_brush
				);

			}
			if (SUCCEEDED(hr))
			{
				hr = m_pRenderTarget->CreateBitmapBrush(
					m_pOtherCharacter2,
					propertiesXClampYClamp,
					&m_pOtherCharacter2_brush
				);

			}
			if (SUCCEEDED(hr))
			{
				hr = m_pRenderTarget->CreateBitmapBrush(
					m_pOtherCharacter3,
					propertiesXClampYClamp,
					&m_pOtherCharacter3_brush
				);

			}
			if (SUCCEEDED(hr))
			{
				hr = m_pRenderTarget->CreateBitmapBrush(
					m_pOtherCharacter4,
					propertiesXClampYClamp,
					&m_pOtherCharacter4_brush
				);

			}
			if (SUCCEEDED(hr))
			{
				hr = m_pRenderTarget->CreateBitmapBrush(
					m_pOtherCharacter5,
					propertiesXClampYClamp,
					&m_pOtherCharacter5_brush
				);

			}
			if (SUCCEEDED(hr))
			{
				hr = m_pRenderTarget->CreateBitmapBrush(
					m_pOtherCharacter6,
					propertiesXClampYClamp,
					&m_pOtherCharacter6_brush
				);

			}
			//��ǳ��
			if (SUCCEEDED(hr))
			{
				hr = m_pRenderTarget->CreateBitmapBrush(
					m_pspeech_bubble,
					propertiesXClampYClamp,
					&m_pspeech_bubble_brush
				);

			}
			//��������
			if (SUCCEEDED(hr))
			{
				hr = m_pRenderTarget->CreateBitmapBrush(
					m_pizza_dow,
					propertiesXClampYClamp,
					&m_pizza_dow_brush
				);
				hr = m_pRenderTarget->CreateBitmapBrush(
					m_pizza_cheese,
					propertiesXClampYClamp,
					&m_pizza_cheese_brush
				);
				hr = m_pRenderTarget->CreateBitmapBrush(
					m_pizza_mushroom,
					propertiesXClampYClamp,
					&m_pizza_mushroom_brush
				);
				hr = m_pRenderTarget->CreateBitmapBrush(
					m_pizza_onion,
					propertiesXClampYClamp,
					&m_pizza_onion_brush
				);
				hr = m_pRenderTarget->CreateBitmapBrush(
					m_pizza_olive,
					propertiesXClampYClamp,
					&m_pizza_olive_brush
				);
				hr = m_pRenderTarget->CreateBitmapBrush(
					m_pizza_pepperoni,
					propertiesXClampYClamp,
					&m_pizza_pepperoni_brush
				);
				hr = m_pRenderTarget->CreateBitmapBrush(
					m_pizza_pmang,
					propertiesXClampYClamp,
					&m_pizza_pmang_brush
				);

			}

			//��������ũ
			if (SUCCEEDED(hr))
			{
				ID2D1GradientStopCollection* pGradientStops = NULL;

				static const D2D1_GRADIENT_STOP gradientStops[] =
				{
					{   0.f,  D2D1::ColorF(D2D1::ColorF::Black, 1.0f)  },
					{   1.f,  D2D1::ColorF(D2D1::ColorF::White, 0.0f)  },
				};

				hr = m_pRenderTarget->CreateGradientStopCollection(gradientStops, 2, &pGradientStops);

				if (SUCCEEDED(hr))
				{
					hr = m_pRenderTarget->CreateLinearGradientBrush(
						D2D1::LinearGradientBrushProperties(D2D1::Point2F(300, 300), D2D1::Point2F(800, 800)),
						pGradientStops, &m_pLinearGradientBrush);
				}

				if (SUCCEEDED(hr))
				{
					hr = m_pRenderTarget->CreateRadialGradientBrush(
						D2D1::RadialGradientBrushProperties(D2D1::Point2F(75, 75), D2D1::Point2F(0, 0), 800, 800),
						pGradientStops, &m_pRadialGradientBrush);
				}
				pGradientStops->Release();
			}
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
	SAFE_RELEASE(m_white);

	//����ڽ�
	SAFE_RELEASE(m_pTransparentBox);

	//��Ʈ�ʺ귯��
	SAFE_RELEASE(m_pOtherCharacter1_brush);
	SAFE_RELEASE(m_pOtherCharacter2_brush);
	SAFE_RELEASE(m_pOtherCharacter3_brush);
	SAFE_RELEASE(m_pOtherCharacter4_brush);
	SAFE_RELEASE(m_pOtherCharacter5_brush);
	SAFE_RELEASE(m_pOtherCharacter6_brush);

	SAFE_RELEASE(m_pizza_dow_brush);
	SAFE_RELEASE(m_pizza_cheese_brush);
	SAFE_RELEASE(m_pizza_mushroom_brush);
	SAFE_RELEASE(m_pizza_onion_brush);
	SAFE_RELEASE(m_pizza_olive_brush);
	SAFE_RELEASE(m_pizza_pepperoni_brush);
	SAFE_RELEASE(m_pizza_pmang_brush);

	SAFE_RELEASE(m_pspeech_bubble_brush);
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

//string -> wstring���� ��ȯ�� ���� �Լ�
std::wstring strconv(const std::string& _src)
{
	USES_CONVERSION;
	return std::wstring(A2W(_src.c_str()));

};

static float float_time_draw_guest = 0.0f;
static float total_time_draw_guest = 0.0f;
//�մԱ׸���
void DemoApp::draw_guest(HRESULT hr, D2D1_SIZE_F rtSize)
{
	//�ִϸ��̼�
	float length = m_Animation.GetValue(float_time_draw_guest);

	//�ð��� ���� �Ÿ����
	D2D1_POINT_2F point, spoint;
	D2D1_POINT_2F tangent;

	hr = guest_PathGeometry->ComputePointAtLength(
		length,
		NULL,
		&point,
		&tangent);

	//�մ��� ��� ���� �׸���
	D2D1::Matrix3x2F tmp = D2D1::Matrix3x2F::Translation(
		point.x - 100,
		point.y - 200
	);

	D2D1_RECT_F rcBrushRect = D2D1::RectF(0, 0, rtSize.width, rtSize.height);

	//������
	D2D1_MATRIX_3X2_F scale = D2D1::Matrix3x2F::Scale(D2D1::Size(0.32f, 0.32f), D2D1::Point2F(0, 0));

	//�մ��� ��� ���� ���� �׸��� (�׸� ���� ��)
	m_pRenderTarget->SetTransform(scale * tmp);
	m_pRenderTarget->FillRectangle(&rcBrushRect, character_tmp);
	
	if (float_time_draw_guest >= m_Animation.GetDuration())
	{
		float_time_draw_guest = 0.0f;
	}
	else
	{
		if (float_time_draw_guest < 9.9) {
			float_time_draw_guest += 0.1;
			pizzacat->stateMachine->setCurrentStateID(0);
			if (float_time_draw_guest == 0.5) {
				//������ �Ҹ�
				soundManager->play(3, false);
			}
		}
		else {

			//stand���·� ���ѻ��� ����
			pizzacat->stateMachine->setCurrentStateID(1);
			guest_timer = guest_timer + 0.016667;

			m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
			D2D1_MATRIX_3X2_F scale = D2D1::Matrix3x2F::Scale(D2D1::Size(0.4f, 0.4f), D2D1::Point2F(0, 0));
			D2D1_MATRIX_3X2_F translation = D2D1::Matrix3x2F::Translation(1050, 250);
			m_pRenderTarget->SetTransform(scale * translation);
			m_pRenderTarget->SetAntialiasMode(D2D1_ANTIALIAS_MODE_ALIASED);
			m_pRenderTarget->FillGeometry(m_pRectGeo1, m_pspeech_bubble_brush, m_pRadialGradientBrush);
			m_pRenderTarget->SetAntialiasMode(D2D1_ANTIALIAS_MODE_PER_PRIMITIVE);
			m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());

			WCHAR mouse_text[100];
			swprintf_s(mouse_text, L"���� �ּ���.\n[�䱸���� : %d��]\n[�ϼ� : %d�� / %d��]", orderpizza, makepizza, orderpizza);

			m_pRenderTarget->DrawText(
				mouse_text,
				wcslen(mouse_text),
				m_pTextFormat_bigsize,
				D2D1::RectF(1110.0f, 310.0f, 1370.0f, 500.0f),
				m_pTextBrush_black
			);
		}
	}
}

//�մ�_������ �׸���
void DemoApp::draw_guest_back(HRESULT hr, D2D1_SIZE_F rtSize)
{
	//�ִϸ��̼�
	static float float_time = 0.0f;
	static float total_time = 0.0f;
	float length = m_Animation1.GetValue(float_time);

	//�ð��� ���� �Ÿ����
	D2D1_POINT_2F point, spoint;
	D2D1_POINT_2F tangent;

	hr = guest_PathGeometry_back->ComputePointAtLength(
		length,
		NULL,
		&point,
		&tangent);

	//�մ��� ��� ���� �׸���
	D2D1::Matrix3x2F tmp = D2D1::Matrix3x2F::Translation(
		point.x - 100,
		point.y - 200
	);

	D2D1_RECT_F rcBrushRect = D2D1::RectF(0, 0, rtSize.width, rtSize.height);

	//������
	D2D1_MATRIX_3X2_F scale = D2D1::Matrix3x2F::Scale(D2D1::Size(0.32f, 0.32f), D2D1::Point2F(0, 0));

	//�մ��� ��� ���� ���� �׸��� (�׸� ���� ��)
	m_pRenderTarget->SetTransform(scale * tmp);
	m_pRenderTarget->FillRectangle(&rcBrushRect, character_tmp);
	if (float_time >= m_Animation1.GetDuration())
	{
		float_time = 0.0f;
	}
	else
	{
		if (float_time < 1.5) {
			//��ǳ��
			m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
			D2D1_MATRIX_3X2_F scale = D2D1::Matrix3x2F::Scale(D2D1::Size(0.4f, 0.4f), D2D1::Point2F(0, 0));
			D2D1_MATRIX_3X2_F translation = D2D1::Matrix3x2F::Translation(1050, 250);
			m_pRenderTarget->SetTransform(scale * translation);
			m_pRenderTarget->SetAntialiasMode(D2D1_ANTIALIAS_MODE_ALIASED);
			m_pRenderTarget->FillGeometry(m_pRectGeo1, m_pspeech_bubble_brush, m_pRadialGradientBrush);
			m_pRenderTarget->SetAntialiasMode(D2D1_ANTIALIAS_MODE_PER_PRIMITIVE);

			m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());

			WCHAR mouse_text[100];
			if (pizzacat->stateMachine->getCurrentStateID() == 2) {
				swprintf_s(mouse_text, L"�����մϴ�.");
				success_voice();
				//soundManager->play(4, false);
			}
			else if (pizzacat->stateMachine->getCurrentStateID() == 3) {
				swprintf_s(mouse_text, L"�ð��ʰ�....");
				fail_voice();
				//soundManager->play(5, false);
			}
			else {
				swprintf_s(mouse_text, L"");
			}

			m_pRenderTarget->DrawText(
				mouse_text,
				wcslen(mouse_text),
				m_pTextFormat_bigsize,
				D2D1::RectF(1110.0f, 340.0f, 1370.0f, 500.0f),
				m_pTextBrush_black
			);
			float_time += 0.05;
		}
		else if (float_time >= 1.5 && float_time < 4.8) {
			float_time += 0.05;
		}
		else {
			guest_timer = 0.0;
			islock = false;
			guest_lock = false;
			makepizza = 0;
			float_time = 0.0f;
			total_time = 0.0f;
			float_time_draw_guest = 0.0f;
			total_time_draw_guest = 0.0f;
			pizzacat->stateMachine->setCurrentStateID(4);
			pizzacat->update();
			voice_lock = false;
		}
	}
}

//���� �׸���
void DemoApp::draw_child(HRESULT hr, D2D1_SIZE_F rtSize) 
{
	//�ִϸ��̼�
	static float float_time = 0.0f;
	static float total_time = 0.0f;
	float length = m_Animation2.GetValue(float_time);

	//�ð��� ���� �Ÿ����
	D2D1_POINT_2F point, spoint;
	D2D1_POINT_2F tangent;

	hr = child_PathGeometry->ComputePointAtLength(
		length,
		NULL,
		&point,
		&tangent);

	//������ ��� ���� �׸���
	D2D1::Matrix3x2F tmp = D2D1::Matrix3x2F::Translation(
		point.x - 100,
		point.y - 200
	);

	D2D1_RECT_F rcBrushRect = D2D1::RectF(0, 0, rtSize.width, rtSize.height);

	//������
	D2D1_MATRIX_3X2_F scale = D2D1::Matrix3x2F::Scale(D2D1::Size(0.32f, 0.32f), D2D1::Point2F(0, 0));

	//������ ��� ���� ���� �׸��� (�׸� ���� ��)
	m_pRenderTarget->SetTransform(scale * tmp);
	m_pRenderTarget->FillRectangle(&rcBrushRect, m_pOtherCharacter4_brush);
	if (float_time >= m_Animation2.GetDuration()){
		float_time = 0.0f;
	}
	else{
		float_time += 0.05;
	}
}

//���� ���� ��ܿ� �׸���
void DemoApp::draw_pizza(HRESULT hr, D2D1_SIZE_F rtSize)
{
	m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
	m_pRenderTarget->FillRectangle(D2D1::RectF(
		0,
		0,
		150,
		150), m_pizza_cheese_brush);
	m_pRenderTarget->DrawBitmap(
		m_pizza_dow,
		D2D1::RectF(
			250,
			10,
			250 + 100,
			10 + 100)
	);
	m_pRenderTarget->DrawBitmap(
		m_pizza_cheese,
		D2D1::RectF(
			370,
			10,
			370 + 100,
			10 + 100)
	);
	m_pRenderTarget->DrawBitmap(
		m_pizza_mushroom,
		D2D1::RectF(
			490,
			10,
			490 + 100,
			10 + 100)
	);
	m_pRenderTarget->DrawBitmap(
		m_pizza_onion,
		D2D1::RectF(
			610,
			10,
			610 + 100,
			10 + 100)
	);
	m_pRenderTarget->DrawBitmap(
		m_pizza_olive,
		D2D1::RectF(
			730,
			10,
			730 + 100,
			10 + 100)
	);
	m_pRenderTarget->DrawBitmap(
		m_pizza_pepperoni,
		D2D1::RectF(
			850,
			10,
			850 + 100,
			10 + 100)
	);
	m_pRenderTarget->DrawBitmap(
		m_pizza_pmang,
		D2D1::RectF(
			970,
			10,
			970 + 100,
			10 + 100)
	);
}

void DemoApp::draw_pizza_state(HRESULT hr, D2D1_SIZE_F rtSize)
{
	m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
	D2D1_RECT_F rectangle_tmp;
	for (int i = 1; i < 8; i++) {
		if (pizza_ingredient[i - 1] == 0) {
			m_box_brush04->SetOpacity(0);
		}
		else if (pizza_ingredient[i - 1] == 1) {
			m_box_brush04->SetOpacity(0.333);
		}
		else if (pizza_ingredient[i - 1] == 2) {
			m_box_brush04->SetOpacity(0.666);
		}
		else {
			m_box_brush04->SetOpacity(1);
		}
		rectangle_tmp = D2D1::RectF(top_box_x_point[i], 110, top_box_x_point[i+1], 150);
		m_pRenderTarget->FillRectangle(rectangle_tmp, m_box_brush04);


		WCHAR point_text[100];
		swprintf_s(point_text, L"%d/3", pizza_ingredient[i-1]);
		m_pRenderTarget->DrawText(
			point_text,
			wcslen(point_text),
			m_pTextFormat,
			D2D1::RectF(top_box_x_point[i] + 40, 110 + 10, top_box_x_point[i + 1] + 40, 150 + 10),
			m_pTextBrush_white
		);
	}
}

wchar_t pizza_name[][100] = {
	L"����",
	L"ġ��",
	L"����",
	L"����",
	L"�ø���",
	L"���۷δ�",
	L"�Ǹ�"
};

void DemoApp::draw_pizza_name(HRESULT hr, D2D1_SIZE_F rtSize)
{
	m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
	
	m_white->SetOpacity(0.5);

	D2D1_RECT_F rectangle_tmp;
	for (int i = 1; i < 8; i++) {
		rectangle_tmp = D2D1::RectF(bottom_box_x_point[i], 350, bottom_box_x_point[i + 1], 400);
		m_pRenderTarget->FillRectangle(rectangle_tmp, m_white);

		WCHAR point_text[100];
		if (i == 6) {
			swprintf_s(point_text, L"%s", pizza_name[i - 1]);
			m_pRenderTarget->DrawText(
				point_text,
				wcslen(point_text),
				m_pTextFormat_bigsize,
				D2D1::RectF(bottom_box_x_point[i] + 10, 350 + 10, bottom_box_x_point[i + 1], 400 + 10),
				m_pTextBrush_black
			);
		}
		else {
			swprintf_s(point_text, L"%s", pizza_name[i - 1]);
			m_pRenderTarget->DrawText(
				point_text,
				wcslen(point_text),
				m_pTextFormat_bigsize,
				D2D1::RectF(bottom_box_x_point[i] + 20, 350 + 10, bottom_box_x_point[i + 1] + 20, 400 + 10),
				m_pTextBrush_black
			);
		}
	}
	rectangle_tmp = D2D1::RectF(900, 350, 1100, 400);
	m_pRenderTarget->FillRectangle(rectangle_tmp, m_white);
	WCHAR point_text[100];
	swprintf_s(point_text, L"�ϼ��ϱ�");
	m_pRenderTarget->DrawText(
		point_text,
		wcslen(point_text),
		m_pTextFormat_bigsize,
		D2D1::RectF(920 + 40, 350 + 10, 1100 + 40, 400 + 10),
		m_pTextBrush_black
	);

	//�ֻ�� ������ ����ũ
	rectangle_tmp = D2D1::RectF(480, 250, 1100, 350);
	m_pRenderTarget->FillRectangle(rectangle_tmp, m_white);
}


void DemoApp::order_pizza()
{
	if (!islock) {
		//���� 1~4�� ���� �ֹ�
		orderpizza = rand() % 4 + 1;

		//ĳ���� ��ȣ ���� ����
		int character_rand = rand() % 6 + 1;
		if (character_rand == 1) {
			character_tmp = m_pOtherCharacter1_brush;
		}
		else if (character_rand == 2) {
			character_tmp = m_pOtherCharacter2_brush;
		}
		else if (character_rand == 3) {
			character_tmp = m_pOtherCharacter3_brush;
		}
		else if (character_rand == 4) {
			character_tmp = m_pOtherCharacter4_brush;
		}
		else if (character_rand == 5) {
			character_tmp = m_pOtherCharacter5_brush;
		}
		else {
			character_tmp = m_pOtherCharacter6_brush;
		}
		islock = true;
	}
}

void DemoApp::success_voice()
{
	if (!voice_lock) {
		voice_lock = true;
		int success_num = rand() % 3;
		switch (success_num) {
		case 0:
			soundManager->play(8, false);
			break;
		case 1:
			soundManager->play(9, false);
			break;
		case 2:
			soundManager->play(10, false);
			break;
		default:
			break;
		}
	}
}

void DemoApp::fail_voice()
{
	if (!voice_lock) {
		voice_lock = true;
		int fail_num = rand() % 3;
		switch (fail_num) {
		case 0:
			soundManager->play(11, false);
			break;
		case 1:
			soundManager->play(12, false);
			break;
		case 2:
			soundManager->play(13, false);
			break;
		default:
			break;
		}
	}
}


// Ŭ���̾�Ʈ ������ ȭ���� �׸�.
// ����: �� �Լ��� ����Ǵ� ���ȿ� ��ġ�� �ҽǵǸ� ��ġ ������ �ڿ����� �ݳ���. �� ���� ȣ�� �ÿ� �� �ڿ����� �ٽ� ������.
HRESULT DemoApp::OnRender()
{
	HRESULT hr = S_OK;

	hr = CreateDeviceResources();

	if (SUCCEEDED(hr))
	{
		//���� cpu�� �̿��Ͽ� �� ����
		//���� ���ٸ� + 0.016667 ���
		/*if (isfirst) {
			isfirst = false;
			start_time = GetTickCount64();
		}*/
		//play_time = (GetTickCount64() - start_time) / 1000;

		D2D1_SIZE_F rtSize = m_pRenderTarget->GetSize();

		//BeginDraw -> �׸��� -> EndDraw
		m_pRenderTarget->BeginDraw();
		m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
		m_pRenderTarget->Clear(D2D1::ColorF(D2D1::ColorF::White));

		//HW3 ���׸���
		D2D1_SIZE_F size = m_pBitmap->GetSize();
		D2D1_POINT_2F upperLeftCorner = D2D1::Point2F(0.f, 0.f);
		m_pRenderTarget->DrawBitmap(
			m_pBitmap,
			D2D1::RectF(
				upperLeftCorner.x,
				upperLeftCorner.y,
				upperLeftCorner.x + size.width,
				upperLeftCorner.y + size.height)
		);
		m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());


		//ĳ���ͱ׸���
		D2D1::Matrix3x2F mycharacter_matrix;
		mycharacter_size = m_pMyCharacter->GetPixelSize();
		m_pRenderTarget->DrawBitmap(
			m_pMyCharacter,
			D2D1::RectF(
				mycharacter_point.x,
				mycharacter_point.y,
				mycharacter_point.x + 150,
				mycharacter_point.y + 250)
		);
		m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());

		
		//å���� �ٽ� �׸���
		m_pRenderTarget->DrawBitmap(
			m_pBitmap_desk,
			D2D1::RectF(
				upperLeftCorner.x,
				upperLeftCorner.y,
				upperLeftCorner.x + size.width,
				upperLeftCorner.y + size.height)
		);

		//������ ����ǥ��
		m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());

		WCHAR sound_t[2][20] = {L"TURE", L"FALSE"};
		WCHAR sound_tt[100];
		if (sound_true) {
			wcscpy(sound_tt, sound_t[1]);
		}
		else {
			wcscpy(sound_tt, sound_t[0]);
		}

		WCHAR point_text[200];
		swprintf_s(point_text, 
			L"201701531_�����_����4\n���� ���� ���� : %d��\n�� �Ǹž� : %d��\n���� Ƚ�� : %d\n���� Ƚ�� : %d\n(��� ���� ���� (TŰ))\n(����Ű �¿�Ű�� �����̽���)\n���� ũ��(����Ű +,-) : %d\n���Ұ� ����(����Ű Y) : %S", 
			score, score * 15000, pizzacat->success_count, pizzacat->fail_count, sound_volume, sound_tt);
		m_pRenderTarget->DrawText(
			point_text,
			wcslen(point_text),
			m_pTextFormat_bigsize,
			D2D1::RectF(1200.0f, 30.0f, 1500.0f, 200.0f),
			m_pTextBrush_white
		);

		//���� ���� ���� ����
		order_pizza();
		//�ִϸ��̼�
		if (makepizza < orderpizza && !guest_lock) {
			draw_guest(hr, rtSize);
		}
		
		m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
		WCHAR point_text_test3[100];
		//�ִϸ��̼�
		if (makepizza >= orderpizza && guest_timer <= orderpizza * pizza_per_time) {
			guest_lock = true;
			pizzacat->stateMachine->setCurrentStateID(2);
			pizzacat->update();

			draw_guest_back(hr, rtSize);
		}
		else if(guest_timer > orderpizza * pizza_per_time){
			guest_lock = true;
			pizzacat->stateMachine->setCurrentStateID(3);
			pizzacat->update();

			draw_guest_back(hr, rtSize);
		}



		m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
		//����ĳ����
		draw_child(hr, rtSize);
		m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
		//�������
		draw_pizza(hr, rtSize);

		//����������
		draw_pizza_state(hr, rtSize);

		//��������̸�
		draw_pizza_name(hr, rtSize);

		//���̺�1
		m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
		m_pRenderTarget->DrawBitmap(
			m_ptables1,
			D2D1::RectF(
				upperLeftCorner.x,
				upperLeftCorner.y,
				upperLeftCorner.x + size.width,
				upperLeftCorner.y + size.height)
		);

		if (pizzacat->stateMachine->getCurrentStateID() == 2) {
			swprintf_s(point_text_test3,
				L"����");
			m_pRenderTarget->DrawText(
				point_text_test3,
				wcslen(point_text_test3),
				m_pTextFormat_bigsize,
				D2D1::RectF(700.0f, 300.0f, 780.0f, 350.0f),
				m_pTextBrush_black
			);
		}
		else if (pizzacat->stateMachine->getCurrentStateID() == 3) {
			swprintf_s(point_text_test3,
				L"����");
			m_pRenderTarget->DrawText(
				point_text_test3,
				wcslen(point_text_test3),
				m_pTextFormat_bigsize,
				D2D1::RectF(700.0f, 300.0f, 780.0f, 350.0f),
				m_pTextBrush_black
			);
		}

		//ĳ����2 (HW4)
		m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
		m_pRenderTarget->DrawBitmap(
			m_pOtherCharacter2,
			followcat->Source
		);

		//X��ǥ�� ���� �� (���� +- 20)
		if (abs(followcat->Source.left - mycharacter_point.x) <= 20) {
			followcat->stateMachine->setCurrentStateID(0);
		}//X��ǥ�� �ٸ� �� (�i�ư��� ��)
		else {
			followcat->stateMachine->setCurrentStateID(1);			
		}

		WCHAR point_text_test[100];
		if (followcat->stateMachine->getCurrentStateID() == 0) {
			swprintf_s(point_text_test,
				L"AI�� ��ġ��\n�÷��̾�� ����");
			m_pRenderTarget->DrawText(
				point_text_test,
				wcslen(point_text_test),
				m_pTextFormat_bigsize,
				D2D1::RectF(500.0f, 280.0f, 700.0f, 350.0f),
				m_pTextBrush_black
			);
		}
		else if (followcat->stateMachine->getCurrentStateID() == 1) {
			swprintf_s(point_text_test,
				L"AI : %.2f\n�÷��̾� : %.2f", followcat->Source.left, mycharacter_point.x);
			m_pRenderTarget->DrawText(
				point_text_test,
				wcslen(point_text_test),
				m_pTextFormat_bigsize,
				D2D1::RectF(500.0f, 280.0f, 700.0f, 350.0f),
				m_pTextBrush_black
			);
		}
		followcat->update(mycharacter_point);

		WCHAR point_text_test2[200];
		swprintf_s(point_text_test2,
			L"���ѽð�: %d��\n����� �ð�: %.2f", orderpizza * pizza_per_time, guest_timer);
		m_pRenderTarget->DrawText(
			point_text_test2,
			wcslen(point_text_test2),
			m_pTextFormat_bigsize,
			D2D1::RectF(800.0f, 280.0f, 1100.0f, 350.0f),
			m_pTextBrush_black
		);


		//���̺�2
		m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
		m_pRenderTarget->DrawBitmap(
			m_ptables2,
			D2D1::RectF(
				upperLeftCorner.x,
				upperLeftCorner.y,
				upperLeftCorner.x + size.width,
				upperLeftCorner.y + size.height)
		);

		m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());

		//��ܱ��� �׸���
		m_pRenderTarget->DrawGeometry(top_PathGeometry, m_box_brush06, 4);

		if (isshow)
		{
			//�մ԰�α��� �׸���
			m_pRenderTarget->DrawGeometry(guest_PathGeometry, m_box_brush07, 4);
			//�մ�_�����ΰ�α��� �׸���
			m_pRenderTarget->DrawGeometry(guest_PathGeometry_back, m_box_brush02, 4);
			//���̰�α��� �׸���
			m_pRenderTarget->DrawGeometry(child_PathGeometry, m_box_brush03, 4);
		}

		m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
		m_box_brush01->SetOpacity(0.5);
		//�ϴܱ��� �׸���
		m_pRenderTarget->DrawGeometry(bottom_PathGeometry, m_box_brush01, 4);

		////�»�� ĳ���� ��ġ ǥ���ϱ�
		m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
		WCHAR mouse_text[100];
		swprintf_s(mouse_text, L"ĳ���� X��ǥ : %.2f \nĳ���� Y��ǥ : %.2f"
			, mycharacter_point.x, mycharacter_point.y);

		m_pRenderTarget->DrawText(
			mouse_text,
			wcslen(mouse_text),
			m_pTextFormat,
			D2D1::RectF(10.0f, 10.0f, 200.0f, 200.0f),
			m_pTextBrush_white
		); //�»�ܿ� �������� ���� ����

		//�׽�Ʈ����
		/*m_pRenderTarget->SetTransform(D2D1::Matrix3x2F::Identity());
		D2D1_MATRIX_3X2_F scale = D2D1::Matrix3x2F::Scale(D2D1::Size(0.4f, 0.4f), D2D1::Point2F(0, 0));
		D2D1_MATRIX_3X2_F translation = D2D1::Matrix3x2F::Translation(1050, 250);
		m_pRenderTarget->SetTransform(scale * translation);
		m_pRenderTarget->SetAntialiasMode(D2D1_ANTIALIAS_MODE_ALIASED);
		m_pRenderTarget->FillGeometry(m_pRectGeo1, m_pspeech_bubble_brush, m_pRadialGradientBrush);
		m_pRenderTarget->SetAntialiasMode(D2D1_ANTIALIAS_MODE_PER_PRIMITIVE);*/

		//����Ŭ����
		//������ Ƚ���� 20ȸ�� ���� ��
		if (pizzacat->success_count > game_clear_count && !gamefail) {
			gameclear = true;
			for (int i = 0; i < 6; i++) {
				soundManager->stop(i);
			}
			for (int i = 7; i < 14; i++) {
				soundManager->stop(i);
			}

			//����
			if (!sound_one_time) {
				soundManager->play(6, false);
				sound_one_time = true;
			}

			m_pRenderTarget->DrawBitmap(
				m_pBitmap_gameclear,
				D2D1::RectF(
					upperLeftCorner.x,
					upperLeftCorner.y,
					upperLeftCorner.x + size.width,
					upperLeftCorner.y + size.height)
			);
		}

		//���ӿ���
		//������ Ƚ���� 5ȸ�� ���� ��
		if (pizzacat->fail_count > game_fail_count && !gameclear) {
			gamefail = true;
			for (int i = 0; i < 7; i++) {
				soundManager->stop(i);
			}
			for (int i = 8; i < 14; i++) {
				soundManager->stop(i);
			}

			//����
			if (!sound_one_time) {
				soundManager->play(7, false);
				sound_one_time = true;
			}

			m_pRenderTarget->DrawBitmap(
				m_pBitmap_gameover,
				D2D1::RectF(
					upperLeftCorner.x,
					upperLeftCorner.y,
					upperLeftCorner.x + size.width,
					upperLeftCorner.y + size.height)
			);
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

		//Ű����
		if (GetAsyncKeyState(VK_LEFT) & 0x8000)
		{
			if (mycharacter_point.x >= 120) {
				mycharacter_point.x -= 5;
				mycharacter_point.y += 0.1;
			}
			InvalidateRect(hwnd, NULL, false);
		}
		if (GetAsyncKeyState(VK_RIGHT) & 0x8000)
		{
			if (mycharacter_point.x <= 1050) {
				mycharacter_point.x += 5;
				mycharacter_point.y -= 0.1;
			}
			InvalidateRect(hwnd, NULL, false);
		}
		if (GetAsyncKeyState(VK_UP) & 0x8000)
		{}
		if (GetAsyncKeyState(VK_DOWN) & 0x8000)
		{}
		//if (GetAsyncKeyState(VK_SPACE) & 0x8000)
		//{
		//	//pDemoApp->soundManager->stop(1);
		//	//pDemoApp->soundManager->play(1, false);
		//}
		
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
				// ���⿡�� ValidateRect�� ȣ������ ���ƾ� OnRender �Լ��� ��� �ݺ� ȣ���.
				//ValidateRect(hwnd, NULL);
			}
			result = 0;
			wasHandled = true;
			break;

			////���콺 ������ ��
			//case WM_MOUSEMOVE:
			//{
			//	return 0;
			//}
			////���콺 ���� ��ư�� �ö� �� (�� �巡�� �ϴٰ� ����)
			//case WM_LBUTTONUP: // ���� �巡�װ� �������� 
			//{
			//	break;
			//}
			////���� ���콺 Ŭ�� ��
			//case WM_LBUTTONDOWN:
			//{
			//	break;
			//}
			//Ű ���� ��
			case WM_KEYDOWN:
			{
				WCHAR input_str;
				input_str = (TCHAR)wParam;
				switch (input_str)
				{
					case VK_SPACE:
					{	
						//����
						if (mycharacter_point.x >= bottom_box_x_point[0] + 45 && mycharacter_point.x < bottom_box_x_point[1] + 45) {
							if (pizza_ingredient[0] < 3) {
								pizza_ingredient[0]++;
								pDemoApp->soundManager->stop(1);
								pDemoApp->soundManager->play(1, false);
							}
						}
						//ġ��
						else if (mycharacter_point.x >= bottom_box_x_point[1] + 45 && mycharacter_point.x < bottom_box_x_point[2] + 45) {
							if (pizza_ingredient[1] < 3) {
								pizza_ingredient[1]++;
								pDemoApp->soundManager->stop(1);
								pDemoApp->soundManager->play(1, false);
							}
						}
						//����
						else if (mycharacter_point.x >= bottom_box_x_point[2] + 45 && mycharacter_point.x < bottom_box_x_point[3] + 45) {
							if (pizza_ingredient[2] < 3) {
								pizza_ingredient[2]++;
								pDemoApp->soundManager->stop(1);
								pDemoApp->soundManager->play(1, false);
							}
						}
						//����
						else if (mycharacter_point.x >= bottom_box_x_point[3] + 45 && mycharacter_point.x < bottom_box_x_point[4] + 45) {
							if (pizza_ingredient[3] < 3) {
								pizza_ingredient[3]++;
								pDemoApp->soundManager->stop(1);
								pDemoApp->soundManager->play(1, false);
							}
						}
						//�ø���
						else if (mycharacter_point.x >= bottom_box_x_point[4] + 45 && mycharacter_point.x < bottom_box_x_point[5] + 45) {
							if (pizza_ingredient[4] < 3) {
								pizza_ingredient[4]++;
								pDemoApp->soundManager->stop(1);
								pDemoApp->soundManager->play(1, false);
							}
						}
						//����δ�
						else if (mycharacter_point.x >= bottom_box_x_point[5] + 45 && mycharacter_point.x < bottom_box_x_point[6] + 45) {
							if (pizza_ingredient[5] < 3) {
								pizza_ingredient[5]++;
								pDemoApp->soundManager->stop(1);
								pDemoApp->soundManager->play(1, false);
							}
						}
						//�Ǹ�
						else if (mycharacter_point.x >= bottom_box_x_point[6] + 45 && mycharacter_point.x < bottom_box_x_point[7] + 45) {
							if (pizza_ingredient[6] < 3) {

								pizza_ingredient[6]++;
								pDemoApp->soundManager->stop(1);
								pDemoApp->soundManager->play(1, false);

								
							}
						}
						//�մ�����
						else if (mycharacter_point.x >= bottom_box_x_point[7] + 45 && mycharacter_point.x < 1045) {
							boolean makefin = true;
							for (int i = 0; i < 7; i++) {
								if (pizza_ingredient[i] != 3) {
									makefin = false;
									break;
								}
							}
							if (makefin) {
								//���Ҹ�
								pDemoApp->soundManager->play(2, false);
								score++;
								makepizza++;
								for (int i = 0; i < 7; i++) {
									pizza_ingredient[i] = 0;
								}
							}
							else {
							}
						}
						else {
						}					
					}
					break;
					case VK_ADD:
						if (sound_volume < 100) {
							sound_volume = sound_volume + 10;
							pDemoApp->soundManager->pDSBPrimary->SetVolume(DSVOLUME_TO_DB(sound_volume));
						}
					break;
					case VK_SUBTRACT:
						if (sound_volume > 0) {
							sound_volume = sound_volume - 10;
							pDemoApp->soundManager->pDSBPrimary->SetVolume(DSVOLUME_TO_DB(sound_volume));
						}
					break;
					case 'y':
					case 'Y':
					{
						sound_true = !sound_true;
						if (sound_true) {
							pDemoApp->soundManager->play(0, true);
							pDemoApp->soundManager->pDSBPrimary->SetVolume(DSVOLUME_TO_DB(sound_volume));
						}
						else {
							pDemoApp->soundManager->stop(0);
							pDemoApp->soundManager->pDSBPrimary->SetVolume(DSVOLUME_TO_DB(0));
						}
						break;
					}
					default:
					{}
					break;
					case 't':
					case 'T':
						isshow = !isshow;
						break;
				}
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

// Creates a Direct2D bitmap from a resource in the application resource file.
HRESULT DemoApp::LoadBitmapFromResource(ID2D1RenderTarget* pRenderTarget, IWICImagingFactory* pIWICFactory, PCWSTR resourceName, PCWSTR resourceType, UINT destinationWidth, UINT destinationHeight, ID2D1Bitmap** ppBitmap)
{
	HRESULT hr = S_OK;
	IWICBitmapDecoder* pDecoder = NULL;
	IWICBitmapFrameDecode* pSource = NULL;
	IWICStream* pStream = NULL;
	IWICFormatConverter* pConverter = NULL;
	IWICBitmapScaler* pScaler = NULL;

	HRSRC imageResHandle = NULL;
	HGLOBAL imageResDataHandle = NULL;
	void* pImageFile = NULL;
	DWORD imageFileSize = 0;

	// Locate the resource.
	imageResHandle = FindResourceW(HINST_THISCOMPONENT, resourceName, resourceType);

	hr = imageResHandle ? S_OK : E_FAIL;
	if (SUCCEEDED(hr))
	{
		// Load the resource.
		imageResDataHandle = LoadResource(HINST_THISCOMPONENT, imageResHandle);

		hr = imageResDataHandle ? S_OK : E_FAIL;
	}

	if (SUCCEEDED(hr))
	{
		// Lock it to get a system memory pointer.
		pImageFile = LockResource(imageResDataHandle);

		hr = pImageFile ? S_OK : E_FAIL;
	}

	if (SUCCEEDED(hr))
	{
		// Calculate the size.
		imageFileSize = SizeofResource(HINST_THISCOMPONENT, imageResHandle);

		hr = imageFileSize ? S_OK : E_FAIL;
	}

	if (SUCCEEDED(hr))
	{
		// Create a WIC stream to map onto the memory.
		hr = pIWICFactory->CreateStream(&pStream);
	}

	if (SUCCEEDED(hr))
	{
		// Initialize the stream with the memory pointer and size.
		hr = pStream->InitializeFromMemory(
			reinterpret_cast<BYTE*>(pImageFile),
			imageFileSize
		);
	}

	if (SUCCEEDED(hr))
	{
		// Create a decoder for the stream.
		hr = pIWICFactory->CreateDecoderFromStream(
			pStream,
			NULL,
			WICDecodeMetadataCacheOnLoad,
			&pDecoder
		);
	}

	if (SUCCEEDED(hr))
	{
		// Create the initial frame.
		hr = pDecoder->GetFrame(0, &pSource);
	}

	if (SUCCEEDED(hr))
	{
		// Convert the image format to 32bppPBGRA
		// (DXGI_FORMAT_B8G8R8A8_UNORM + D2D1_ALPHA_MODE_PREMULTIPLIED).
		hr = pIWICFactory->CreateFormatConverter(&pConverter);
	}

	if (SUCCEEDED(hr))
	{
		// If a new width or height was specified, create an
		// IWICBitmapScaler and use it to resize the image.
		if (destinationWidth != 0 || destinationHeight != 0)
		{
			UINT originalWidth, originalHeight;
			hr = pSource->GetSize(&originalWidth, &originalHeight);
			if (SUCCEEDED(hr))
			{
				if (destinationWidth == 0)
				{
					FLOAT scalar = static_cast<FLOAT>(destinationHeight) / static_cast<FLOAT>(originalHeight);
					destinationWidth = static_cast<UINT>(scalar * static_cast<FLOAT>(originalWidth));
				}
				else if (destinationHeight == 0)
				{
					FLOAT scalar = static_cast<FLOAT>(destinationWidth) / static_cast<FLOAT>(originalWidth);
					destinationHeight = static_cast<UINT>(scalar * static_cast<FLOAT>(originalHeight));
				}

				hr = pIWICFactory->CreateBitmapScaler(&pScaler);
				if (SUCCEEDED(hr))
				{
					hr = pScaler->Initialize(
						pSource,
						destinationWidth,
						destinationHeight,
						WICBitmapInterpolationModeCubic
					);
				}

				if (SUCCEEDED(hr))
				{
					hr = pConverter->Initialize(
						pScaler,
						GUID_WICPixelFormat32bppPBGRA,
						WICBitmapDitherTypeNone,
						NULL,
						0.f,
						WICBitmapPaletteTypeMedianCut
					);
				}
			}
		}
		else
		{
			hr = pConverter->Initialize(
				pSource,
				GUID_WICPixelFormat32bppPBGRA,
				WICBitmapDitherTypeNone,
				NULL,
				0.f,
				WICBitmapPaletteTypeMedianCut
			);
		}
	}

	if (SUCCEEDED(hr))
	{
		//create a Direct2D bitmap from the WIC bitmap.
		hr = pRenderTarget->CreateBitmapFromWicBitmap(
			pConverter,
			NULL,
			ppBitmap
		);
	}

	SAFE_RELEASE(pDecoder);
	SAFE_RELEASE(pSource);
	SAFE_RELEASE(pStream);
	SAFE_RELEASE(pConverter);
	SAFE_RELEASE(pScaler);

	return hr;
}

// Creates a Direct2D bitmap from the specified file name.
HRESULT DemoApp::LoadBitmapFromFile(ID2D1RenderTarget* pRenderTarget, IWICImagingFactory* pIWICFactory, PCWSTR uri, UINT destinationWidth, UINT destinationHeight, ID2D1Bitmap** ppBitmap)
{
	HRESULT hr = S_OK;

	IWICBitmapDecoder* pDecoder = NULL;
	IWICBitmapFrameDecode* pSource = NULL;
	IWICStream* pStream = NULL;
	IWICFormatConverter* pConverter = NULL;
	IWICBitmapScaler* pScaler = NULL;

	hr = pIWICFactory->CreateDecoderFromFilename(
		uri,
		NULL,
		GENERIC_READ,
		WICDecodeMetadataCacheOnLoad,
		&pDecoder
	);

	if (SUCCEEDED(hr))
	{
		// Create the initial frame.
		hr = pDecoder->GetFrame(0, &pSource);
	}

	if (SUCCEEDED(hr))
	{
		// Convert the image format to 32bppPBGRA
		// (DXGI_FORMAT_B8G8R8A8_UNORM + D2D1_ALPHA_MODE_PREMULTIPLIED).
		hr = pIWICFactory->CreateFormatConverter(&pConverter);
	}

	if (SUCCEEDED(hr))
	{
		// If a new width or height was specified, create an
		// IWICBitmapScaler and use it to resize the image.
		if (destinationWidth != 0 || destinationHeight != 0)
		{
			UINT originalWidth, originalHeight;
			hr = pSource->GetSize(&originalWidth, &originalHeight);
			if (SUCCEEDED(hr))
			{
				if (destinationWidth == 0)
				{
					FLOAT scalar = static_cast<FLOAT>(destinationHeight) / static_cast<FLOAT>(originalHeight);
					destinationWidth = static_cast<UINT>(scalar * static_cast<FLOAT>(originalWidth));
				}
				else if (destinationHeight == 0)
				{
					FLOAT scalar = static_cast<FLOAT>(destinationWidth) / static_cast<FLOAT>(originalWidth);
					destinationHeight = static_cast<UINT>(scalar * static_cast<FLOAT>(originalHeight));
				}

				hr = pIWICFactory->CreateBitmapScaler(&pScaler);
				if (SUCCEEDED(hr))
				{
					hr = pScaler->Initialize(
						pSource,
						destinationWidth,
						destinationHeight,
						WICBitmapInterpolationModeCubic
					);
				}

				if (SUCCEEDED(hr))
				{
					hr = pConverter->Initialize(
						pScaler,
						GUID_WICPixelFormat32bppPBGRA,
						WICBitmapDitherTypeNone,
						NULL,
						0.f,
						WICBitmapPaletteTypeMedianCut
					);
				}
			}
		}
		else // Don't scale the image.
		{
			hr = pConverter->Initialize(
				pSource,
				GUID_WICPixelFormat32bppPBGRA,
				WICBitmapDitherTypeNone,
				NULL,
				0.f,
				WICBitmapPaletteTypeMedianCut
			);
		}
	}
	if (SUCCEEDED(hr))
	{
		// Create a Direct2D bitmap from the WIC bitmap.
		hr = pRenderTarget->CreateBitmapFromWicBitmap(
			pConverter,
			NULL,
			ppBitmap
		);
	}

	SAFE_RELEASE(pDecoder);
	SAFE_RELEASE(pSource);
	SAFE_RELEASE(pStream);
	SAFE_RELEASE(pConverter);
	SAFE_RELEASE(pScaler);

	return hr;
}