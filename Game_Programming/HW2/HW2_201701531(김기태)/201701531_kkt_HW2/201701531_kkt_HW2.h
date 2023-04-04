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

// �ڿ� ���� ��ȯ ��ũ��.
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

	//�׵θ�
	ID2D1SolidColorBrush* m_border_brush;

	//�ϴܻ��ڸ� �׸� �귯�� ����
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

	//����
	ID2D1SolidColorBrush* m_pTransparentBox;

	//�۾�
	ID2D1SolidColorBrush* m_pTextBrush;

	//Write
	IDWriteFactory* m_pDWriteFactory;
	IDWriteTextFormat* m_pTextFormat;

	//���ڸ� �巡���ϸ� �׸��� �Լ�
	void draw_box();
	void draw_next_box();

	//�巡���ؼ� �簢���� �׸��� �Լ�
	void draw_moving_Rectangle();
};

//�л� ����
const std::string familyNames[] = { "��", "��", "��", "��", "��", "��", "��",
							  "��", "��", "��", "��", "��", "ȫ", "��",
							  "��", "��", "��", "��" };  //���� ����

const std::string firstNames[] = { "��", "��", "��", "��", "��", "ö",
							 "��", "��", "��", "��", "��", "��",
							 "��", "��", "��", "��", "��", "ȣ",
							 "��", "��", "��" };  //���� �̸�

struct student {
	//�̸��� ����
	std::string name;
	//������ 30~90������ ���� (�Ǽ��ƴ�)
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