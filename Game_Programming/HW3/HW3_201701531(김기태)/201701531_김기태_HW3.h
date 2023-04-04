#pragma once

// Windows Header Files:
#include <windows.h>
// C RunTime Header Files:
#include <stdlib.h>
#include <malloc.h>
#include <memory.h>
#include <wchar.h>
#include <iostream>
#include <string>
#include <atlconv.h>
#include <algorithm>
#include <math.h>
// DX Header Files:
#include <d2d1.h>
#include <d2d1helper.h>
#include <dwrite.h>
#include <wincodec.h>
#include <stdio.h>
#include <tchar.h>
#include <time.h>
#include <Dwmapi.h>

#include "Animation.h"

// 자원 안전 반환 매크로.
#define SAFE_RELEASE(p) { if(p) { (p)->Release(); (p)=NULL; } }

// 현재 모듈의 시작주소 얻기.
#ifndef HINST_THISCOMPONENT
EXTERN_C IMAGE_DOS_HEADER __ImageBase;
#define HINST_THISCOMPONENT ((HINSTANCE)&__ImageBase)
#endif

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
	HRESULT LoadBitmapFromResource(ID2D1RenderTarget* pRenderTarget, IWICImagingFactory* pIWICFactory, PCWSTR resourceName, PCWSTR resourceType, UINT destinationWidth, UINT destinationHeight, __deref_out ID2D1Bitmap** ppBitmap);
	HRESULT LoadBitmapFromFile(ID2D1RenderTarget* pRenderTarget, IWICImagingFactory* pIWICFactory, PCWSTR uri, UINT destinationWidth, UINT destinationHeight, __deref_out ID2D1Bitmap** ppBitmap);

private:
	HWND m_hwnd;
	//미리 선언을 해야 움직일 수 있다.
	IWICImagingFactory* m_pWICFactory;
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

	ID2D1SolidColorBrush* m_white;

	//투명
	ID2D1SolidColorBrush* m_pTransparentBox;

	//텍스트
	ID2D1SolidColorBrush* m_pTextBrush_white;
	ID2D1SolidColorBrush* m_pTextBrush_black;
	IDWriteFactory* m_pDWriteFactory;
	IDWriteTextFormat* m_pTextFormat;
	IDWriteTextFormat* m_pTextFormat_bigsize;

	//배경
	ID2D1Bitmap* m_pBitmap;
	ID2D1Bitmap* m_pBitmap_desk;
	ID2D1Bitmap* m_ptables1;
	ID2D1Bitmap* m_ptables2;

	//캐릭터
	ID2D1Bitmap* m_pMyCharacter;
	ID2D1Bitmap* m_pOtherCharacter1;
	ID2D1Bitmap* m_pOtherCharacter2;
	ID2D1Bitmap* m_pOtherCharacter3;
	ID2D1Bitmap* m_pOtherCharacter4;
	ID2D1Bitmap* m_pOtherCharacter5;
	ID2D1Bitmap* m_pOtherCharacter6;

	//말풍선
	ID2D1Bitmap* m_pspeech_bubble;
	ID2D1BitmapBrush* m_pspeech_bubble_brush;

	//피자 재료들
	ID2D1Bitmap* m_pizza_dow;
	ID2D1Bitmap* m_pizza_cheese;
	ID2D1Bitmap* m_pizza_mushroom;
	ID2D1Bitmap* m_pizza_onion;
	ID2D1Bitmap* m_pizza_olive;
	ID2D1Bitmap* m_pizza_pepperoni;
	ID2D1Bitmap* m_pizza_pmang;

	ID2D1BitmapBrush* m_pizza_dow_brush;
	ID2D1BitmapBrush* m_pizza_cheese_brush;
	ID2D1BitmapBrush* m_pizza_mushroom_brush;
	ID2D1BitmapBrush* m_pizza_onion_brush;
	ID2D1BitmapBrush* m_pizza_olive_brush;
	ID2D1BitmapBrush* m_pizza_pepperoni_brush;
	ID2D1BitmapBrush* m_pizza_pmang_brush;

	// Geometry
	ID2D1PathGeometry* top_PathGeometry;
	ID2D1PathGeometry* bottom_PathGeometry;
	ID2D1PathGeometry* guest_PathGeometry;
	ID2D1PathGeometry* guest_PathGeometry_back;
	ID2D1PathGeometry* child_PathGeometry;

	//비트맵브러쉬
	ID2D1BitmapBrush* m_pOtherCharacter1_brush;
	ID2D1BitmapBrush* m_pOtherCharacter2_brush;
	ID2D1BitmapBrush* m_pOtherCharacter3_brush;
	ID2D1BitmapBrush* m_pOtherCharacter4_brush;
	ID2D1BitmapBrush* m_pOtherCharacter5_brush;
	ID2D1BitmapBrush* m_pOtherCharacter6_brush;

	//애니메이션 적용
	AnimationLinear<float> m_Animation;
	AnimationLinear<float> m_Animation1;
	AnimationLinear<float> m_Animation2;

	//시간 계산
	LARGE_INTEGER m_nPrevTime;
	LARGE_INTEGER m_nFrequency;

	void draw_guest(HRESULT hr, D2D1_SIZE_F rtSize);
	void draw_guest_back(HRESULT hr, D2D1_SIZE_F rtSize);

	void draw_child(HRESULT hr, D2D1_SIZE_F rtSize);

	void draw_pizza(HRESULT hr, D2D1_SIZE_F rtSize);
	void draw_pizza_state(HRESULT hr, D2D1_SIZE_F rtSize);
	void draw_pizza_name(HRESULT hr, D2D1_SIZE_F rtSize);

	void order_pizza();

	//불투명마스크
	// Gradients
	ID2D1LinearGradientBrush* m_pLinearGradientBrush;
	ID2D1RadialGradientBrush* m_pRadialGradientBrush;

	// Rectangle Geometry
	ID2D1RectangleGeometry* m_pRectGeo1;

};