#include "SoundManager.h"
#include "201701531_김기태_HW4.h"

BOOL DemoApp::initSound(HWND hWnd)
{
	soundManager = new CSoundManager;

	if (!soundManager->init(hWnd))
		return FALSE;

	// 사운드 파일을 추가함. //id=0부터 시작함.
	int id;
	if (!soundManager->add(L".\\sound\\background.wav", &id))
		return FALSE;

	if (!soundManager->add(L".\\sound\\ingredient_click.wav", &id))
		return FALSE;

	if (!soundManager->add(L".\\sound\\pay.wav", &id))
		return FALSE;

	if (!soundManager->add(L".\\sound\\guest_entrance.wav", &id))
		return FALSE;

	if (!soundManager->add(L".\\sound\\pizza_complete.wav", &id))
		return FALSE;

	if (!soundManager->add(L".\\sound\\pizza_fail.wav", &id))
		return FALSE;

	//extension
	if (!soundManager->add(L".\\sound\\extension\\win.wav", &id))
		return FALSE;

	if (!soundManager->add(L".\\sound\\extension\\gameover.wav", &id))
		return FALSE;

	//8번~10번
	if (!soundManager->add(L".\\sound\\extension\\success\\success1.wav", &id))
		return FALSE;
	if (!soundManager->add(L".\\sound\\extension\\success\\success2.wav", &id))
		return FALSE;
	if (!soundManager->add(L".\\sound\\extension\\success\\success3.wav", &id))
		return FALSE;

	//11번~13번
	if (!soundManager->add(L".\\sound\\extension\\fail\\fail1.wav", &id))
		return FALSE;
	if (!soundManager->add(L".\\sound\\extension\\fail\\fail2.wav", &id))
		return FALSE;
	if (!soundManager->add(L".\\sound\\extension\\fail\\fail3.wav", &id))
		return FALSE;

	return true;
}