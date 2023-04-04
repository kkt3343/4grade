// 201701531_kkt_HW1.cpp : 애플리케이션에 대한 진입점을 정의합니다.
//

#include "framework.h"
#include "201701531_kkt_HW1.h"
#include "hw1_vector.h"

#define MAX_LOADSTRING 100

// 전역 변수:
HINSTANCE hInst;                                // 현재 인스턴스입니다.
WCHAR szTitle[MAX_LOADSTRING];                  // 제목 표시줄 텍스트입니다.
WCHAR szWindowClass[MAX_LOADSTRING];            // 기본 창 클래스 이름입니다.

// 이 코드 모듈에 포함된 함수의 선언을 전달합니다:
ATOM                MyRegisterClass(HINSTANCE hInstance);
BOOL                InitInstance(HINSTANCE, int);
LRESULT CALLBACK    WndProc(HWND, UINT, WPARAM, LPARAM);
INT_PTR CALLBACK    About(HWND, UINT, WPARAM, LPARAM);

int APIENTRY wWinMain(_In_ HINSTANCE hInstance,
                     _In_opt_ HINSTANCE hPrevInstance,
                     _In_ LPWSTR    lpCmdLine,
                     _In_ int       nCmdShow)
{
    UNREFERENCED_PARAMETER(hPrevInstance);
    UNREFERENCED_PARAMETER(lpCmdLine);

    // TODO: 여기에 코드를 입력합니다.

    // 전역 문자열을 초기화합니다.
    LoadStringW(hInstance, IDS_APP_TITLE, szTitle, MAX_LOADSTRING);
    LoadStringW(hInstance, IDC_MY201701531KKTHW1, szWindowClass, MAX_LOADSTRING);
    MyRegisterClass(hInstance);

    // 애플리케이션 초기화를 수행합니다:
    if (!InitInstance (hInstance, nCmdShow))
    {
        return FALSE;
    }

    HACCEL hAccelTable = LoadAccelerators(hInstance, MAKEINTRESOURCE(IDC_MY201701531KKTHW1));

    MSG msg;

    // 기본 메시지 루프입니다:
    while (GetMessage(&msg, nullptr, 0, 0))
    {
        if (!TranslateAccelerator(msg.hwnd, hAccelTable, &msg))
        {
            TranslateMessage(&msg);
            DispatchMessage(&msg);
        }
    }

    return (int) msg.wParam;
}



//
//  함수: MyRegisterClass()
//
//  용도: 창 클래스를 등록합니다.
//
ATOM MyRegisterClass(HINSTANCE hInstance)
{
    WNDCLASSEXW wcex;

    wcex.cbSize = sizeof(WNDCLASSEX);

    wcex.style          = CS_HREDRAW | CS_VREDRAW;
    wcex.lpfnWndProc    = WndProc;
    wcex.cbClsExtra     = 0;
    wcex.cbWndExtra     = 0;
    wcex.hInstance      = hInstance;
    wcex.hIcon          = LoadIcon(hInstance, MAKEINTRESOURCE(IDI_MY201701531KKTHW1));
    wcex.hCursor        = LoadCursor(nullptr, IDC_ARROW);
    wcex.hbrBackground  = (HBRUSH)(COLOR_WINDOW+1);
    wcex.lpszMenuName   = MAKEINTRESOURCEW(IDC_MY201701531KKTHW1);
    wcex.lpszClassName  = szWindowClass;
    wcex.hIconSm        = LoadIcon(wcex.hInstance, MAKEINTRESOURCE(IDI_SMALL));

    return RegisterClassExW(&wcex);
}

//
//   함수: InitInstance(HINSTANCE, int)
//
//   용도: 인스턴스 핸들을 저장하고 주 창을 만듭니다.
//
//   주석:
//
//        이 함수를 통해 인스턴스 핸들을 전역 변수에 저장하고
//        주 프로그램 창을 만든 다음 표시합니다.
//

//창 크기 설정
int windows_witdh = 500;
int windows_height = 800;

BOOL InitInstance(HINSTANCE hInstance, int nCmdShow)
{
   hInst = hInstance; // 인스턴스 핸들을 전역 변수에 저장합니다.

   //타이틀명을 정한다.
   HWND hWnd = CreateWindowW(szWindowClass, L"201701531_김기태_HW1", WS_OVERLAPPEDWINDOW,
      CW_USEDEFAULT, 0, CW_USEDEFAULT, 0, nullptr, nullptr, hInstance, nullptr);

   //윈도우 창의 크기를 정한다.
   RECT rt = { 0, 0, windows_witdh, windows_height };
   SetWindowPos(hWnd, NULL, 50, 50, rt.right - rt.left, rt.bottom - rt.top, 0);

   if (!hWnd)
   {
      return FALSE;
   }

   ShowWindow(hWnd, nCmdShow);
   UpdateWindow(hWnd);

   return TRUE;
}

//
//  함수: WndProc(HWND, UINT, WPARAM, LPARAM)
//
//  용도: 주 창의 메시지를 처리합니다.
//
//  WM_COMMAND  - 애플리케이션 메뉴를 처리합니다.
//  WM_PAINT    - 주 창을 그립니다.
//  WM_DESTROY  - 종료 메시지를 게시하고 반환합니다.
//
//

// 컬러지정
COLORREF color(int i) {
    COLORREF color;
    if (i == 0) color = RGB(165, 102, 255); //보
    else if (i == 1) color = RGB(107, 102, 255); //남
    else if (i == 2) color = RGB(128, 245, 255); //파
    else if (i == 3) color = RGB(134, 229, 127); //초
    else if (i == 4) color = RGB(250, 237, 125); //노
    else if (i == 5) color = RGB(242, 150, 97); //주
    else if (i == 6) color = RGB(241, 95, 95); //빨
    else color = RGB(116, 116, 216);;
    return color;
}

//HW1을 위한 변수 선언
LRESULT OnPaint(HWND hWnd, int index);
student people;
std::vector<student> people_vector;

//사각형의 크기 (처음에 custom 설정)
int square_width = 300;
int square_height = 65;
int padding = 5;

int start_width = (int)(windows_witdh - square_width) / 2;
int start_height = windows_height - 140;

int present_height = start_height + (square_height + padding);

LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam)
{
    switch (message)
    {
    case WM_COMMAND:
        {
            int wmId = LOWORD(wParam);
            // 메뉴 선택을 구문 분석합니다:
            switch (wmId)
            {
            case IDM_ABOUT:
                DialogBox(hInst, MAKEINTRESOURCE(IDD_ABOUTBOX), hWnd, About);
                break;
            case IDM_EXIT:
                DestroyWindow(hWnd);
                break;
            default:
                return DefWindowProc(hWnd, message, wParam, lParam);
            }
        }
        break;
    case WM_PAINT:
        {
            //자동으로 그려짐
            // //       박스너비, 박스높이
            // TODO: 여기에 hdc를 사용하는 그리기 코드를 추가합니다...
            //값 설정
        OnPaint(hWnd, people_vector.size());
        }
        break;
    case WM_DESTROY:
        PostQuitMessage(0);
        break;

    case WM_LBUTTONDOWN:
    {
        //왼쪽 마우스 클릭
        //클릭 좌표값 얻기
        int x = LOWORD(lParam);
        int y = HIWORD(lParam);
        
        int family_random = rand() % (sizeof(familyNames) / sizeof(std::string)); //0 ~ 17
        int first_random1 = rand() % (sizeof(firstNames) / sizeof(std::string)); //0 ~ 21
        int first_random2 = rand() % (sizeof(firstNames) / sizeof(std::string)); //0 ~ 21
        //이름설정
        people.name = familyNames[family_random] + firstNames[first_random1] + firstNames[first_random2];
        //성적설정
        people.score = rand() % 61 + 30;

        //맨위 사각형 누르면 사각형을 지운다.
        if (people_vector.size() != 0 && x >= start_width && x <= windows_witdh - start_width && y >= present_height - square_height && y <= present_height)
        {
            //다시 그리기
            people_vector.pop_back();
            RECT r;
            r.left = 0;
            r.right = windows_witdh;
            r.top = 0;
            r.bottom = start_height + padding;

            present_height = present_height + (padding + square_height);

            InvalidateRect(hWnd, &r, TRUE);
        }
        else
        {        
            //이름과 성적 설정
            if (people_vector.size() < 7) {
                people_vector.push_back(people);

                //정렬
                std::sort(people_vector.begin(), people_vector.end(), compare);
                present_height = present_height - (padding + square_height);
            }
            else {
                MessageBox(hWnd, L"더 이상 추가 할 수 없습니다.", L"크기 초과", MB_OK);
            }
            InvalidateRect(hWnd, NULL, false);
        }
        break;
    }
    case WM_RBUTTONDOWN:
        {
        //오른쪽 마우스 클릭
        }
        break;
    default:
        return DefWindowProc(hWnd, message, wParam, lParam);
    }
    return 0;
}

// 정보 대화 상자의 메시지 처리기입니다.
INT_PTR CALLBACK About(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam)
{
    UNREFERENCED_PARAMETER(lParam);
    switch (message)
    {
    case WM_INITDIALOG:
        return (INT_PTR)TRUE;

    case WM_COMMAND:
        if (LOWORD(wParam) == IDOK || LOWORD(wParam) == IDCANCEL)
        {
            EndDialog(hDlg, LOWORD(wParam));
            return (INT_PTR)TRUE;
        }
        break;
    }
    return (INT_PTR)FALSE;
}

//사각형 그리기 & 텍스트 넣기 함수
LRESULT OnPaint(HWND hWnd, int index)
{
    PAINTSTRUCT ps;
    HDC hdc = BeginPaint(hWnd, &ps);

    //PS_SOLID = 실선
    //int red = 0, green = 0, blue = 0;
   
    //현재 몇개인지 출력
    TCHAR count[30] = { 0 };
    TCHAR count2[] = _T("현재 vector의 개수 : ");
    int tmp_index = index;
    _stprintf_s(count, _countof(count), _T("%d"), tmp_index);
    TextOut(ps.hdc, 20, 40, count2, _tcslen(count2));
    TextOut(ps.hdc, 160, 40, count, _tcslen(count));

    //vector의 개수만큼 네모를 그린다.
    for (int i = 0; i < index; i++) {

        /*red = rand() % 256;
        green = rand() % 256;
        blue = rand() % 256;
        HPEN hMyRandomPen = CreatePen(PS_SOLID, 3, RGB(red, green, blue));
        HGDIOBJ hOldPen = SelectObject(ps.hdc, hMyRandomPen);*/
        HBRUSH mybrush, oldbrush;
        mybrush = CreateSolidBrush(color(i));
        oldbrush = (HBRUSH)SelectObject(ps.hdc, mybrush);
        SetBkColor(ps.hdc, color(i));
        SetBkMode(ps.hdc, TRANSPARENT);

        if (i == 2 || i == 4) {
            SetTextColor(ps.hdc, RGB(0, 0, 0));
        }
        else {
            SetTextColor(ps.hdc, RGB(255, 255, 255));
        }
        //left top right bottom
        Rectangle(ps.hdc, start_width, start_height - ((square_height + padding) * i) - square_height, start_width + square_width, start_height - ((square_height + padding) * i));
        //SelectObject(ps.hdc, hOldPen);

        TCHAR text_name[20] = {};
        TCHAR text_score[10] = { 0 };

        _tcscpy_s(text_name, CA2T(people_vector.at(i).name.c_str()));

        int tmp_score = people_vector.at(i).score;
        _stprintf_s(text_score, _countof(text_score), _T("%d"), tmp_score);

        //텍스트 부분
        TextOut(ps.hdc, start_width + 10, start_height - 40 - ((square_height + padding) * i), text_name, _tcslen(text_name));
        TextOut(ps.hdc, start_width + 110, start_height - 40 - ((square_height + padding) * i), text_score, _tcslen(text_score));
    }
    EndPaint(hWnd, &ps);
    return 0;
}