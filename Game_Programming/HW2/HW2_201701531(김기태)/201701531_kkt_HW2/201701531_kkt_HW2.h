#pragma once

// Windows Header Files:
#include <windows.h>
// C RunTime Header Files:
#include <stdlib.h>
#include <malloc.h>
#include <memory.h>
#include <wchar.h>
#include <math.h>
#include <iostream>
#include <string>
#include <atlconv.h>
#include <algorithm>
// DX Header Files:
#include <d2d1.h>
#include <d2d1helper.h>
#include <dwrite.h>
#include <wincodec.h>
#include <stdio.h>
#include <tchar.h>
#include <vector>
#include <time.h>

// 자원 안전 반환 매크로.
#define SAFE_RELEASE(p) { if(p) { (p)->Release(); (p)=NULL; } }

class DemoApp
{
public:
	DemoApp();
	~DemoApp();
	HRESULT Initialize(HINSTANCE hInstance);  // Register the window class and call methods for instantiating drawing resources
	void RunMessageLoop();  // Process and dispatch messages

private:
	HRESULT CreateDeviceIndependentResources();  // Initialize device-independent resources.
	HRESULT CreateDeviceResources();  // Initialize device-dependent resources.
	void DiscardDeviceResources();  // Release device-dependent resource.
	HRESULT OnRender();   // Draw content.
	void OnResize(UINT width, UINT height);  // Resize the render target.
	static LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam);  // The windows procedure.

private:
	HWND m_hwnd;
	ID2D1Factory* m_pDirect2dFactory;
	ID2D1HwndRenderTarget* m_pRenderTarget;
	ID2D1SolidColorBrush* m_pLightSlateGrayBrush;
	ID2D1SolidColorBrush* m_pCornflowerBlueBrush;

	//테두리
	ID2D1SolidColorBrush* m_border_brush;

	//하단상자를 그릴 브러쉬 설정
	ID2D1SolidColorBrush* m_box_brush01;
	ID2D1SolidColorBrush* m_box_brush02;
	ID2D1SolidColorBrush* m_box_brush03;
	ID2D1SolidColorBrush* m_box_brush04;
	ID2D1SolidColorBrush* m_box_brush05;
	ID2D1SolidColorBrush* m_box_brush06;
	ID2D1SolidColorBrush* m_box_brush07;
	ID2D1SolidColorBrush* m_box_brush08;
	ID2D1SolidColorBrush* m_box_brush09;
	ID2D1SolidColorBrush* m_box_brush10;

	//투명
	ID2D1SolidColorBrush* m_pTransparentBox;

	//글씨
	ID2D1SolidColorBrush* m_pTextBrush;

	//Write
	IDWriteFactory* m_pDWriteFactory;
	IDWriteTextFormat* m_pTextFormat;

	//상자를 드래그하면 그리는 함수
	void draw_box();
	void draw_next_box();

	//드래그해서 사각형을 그리는 함수
	void draw_moving_Rectangle();
};

//학생 정보
const std::string familyNames[] = { "김", "이", "박", "최", "강", "유", "노",
							  "정", "오", "송", "서", "한", "홍", "고",
							  "신", "조", "장", "임" };  //랜덤 성씨

const std::string firstNames[] = { "주", "지", "정", "수", "인", "철",
							 "현", "석", "준", "은", "연", "범",
							 "동", "훈", "영", "소", "하", "호",
							 "진", "명", "윤" };  //랜덤 이름

struct student {
	//이름과 성적
	std::string name;
	//점수는 30~90사이의 정수 (실수아님)
	int score = 0;
};

bool compare(const student& p1, const student& p2) {
	if (p1.score < p2.score) {
		return p1.score > p2.score;
	}
	else if (p1.score < p2.score) {
		return p1.score > p2.score;
	}
	else {
		return p1.score > p2.score;
	}
}