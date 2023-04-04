#pragma once
#include <d2d1.h>
#include <d2d1helper.h>
#include "FiniteStateMachine.h"
#include "201701531_김기태_HW4.h"

class FollowCat
{
public:
	enum StateID {
		STATE_STAND = 0,
		STATE_MOVE
	};

	enum EventID {
		diff_x,
		same_x
	};

	//AI의 시작점 설정
	D2D1_RECT_F Source = {
		300,
		615,
		300 + 150,
		615 + 250 };

	D2D1_POINT_2F Position;
	D2D1_POINT_2F destPosition;
	FiniteStateMachine* stateMachine;

public:
	FollowCat() {
		stateMachine = new FiniteStateMachine();

		//현재상태 -> 상황발생 -> 다음상태
		DWORD StateTransitions[][3] = {
			{STATE_STAND, diff_x, STATE_MOVE},
			{STATE_MOVE, same_x, STATE_STAND}
		};

		//상황의 개수
		for (int i = 0; i < 2; i++) {
			stateMachine->addStateTransition(StateTransitions[i][0],
				StateTransitions[i][1], StateTransitions[i][2]);
		}

		//처음상태
		stateMachine->setCurrentStateID(STATE_STAND);
	}
	void update(D2D1_POINT_2F dest) {
		switch (stateMachine->getCurrentStateID()) {

			//플레이어의 x좌표와 AI의 x좌표를 서로 비교한다.

			//가만히 있을 때
		case STATE_STAND:
			stateMachine->issueEvent(same_x);
			break;
			//움직일 때
		case STATE_MOVE:
			if (dest.x > Source.left) {
				Source.left = Source.left + 1;
				Source.right = Source.right + 1;
			}
			else {
				Source.left = Source.left - 1;
				Source.right = Source.right - 1;
			}
			stateMachine->issueEvent(diff_x);
			break;
		}
	}
};

class PizzaCat
{
public:
	enum StateID {
		STATE_IN = 0,
		STATE_STAND,
		STATE_SUCCESS,
		STATE_FAIL,
		STATE_OUT
	};

	enum EventID {
		entrance_market,
		event_success,
		event_fail,
		out_market,
		event_reset
	};

	FiniteStateMachine* stateMachine;

	bool lock = false;
	int success_count = 0;
	int fail_count = 0;

public:
	PizzaCat() {
		stateMachine = new FiniteStateMachine();

		//현재상태 -> 상황발생 -> 다음상태
		DWORD StateTransitions[][3] = {
			{STATE_IN, entrance_market, STATE_STAND},
			{STATE_STAND, event_success, STATE_SUCCESS},
			{STATE_STAND, event_fail, STATE_FAIL},
			{STATE_SUCCESS, out_market, STATE_IN},
			{STATE_FAIL, out_market, STATE_IN},
			{STATE_OUT, event_reset, STATE_IN}
		};

		//상황의 개수
		for (int i = 0; i < 6; i++) {
			stateMachine->addStateTransition(StateTransitions[i][0],
				StateTransitions[i][1], StateTransitions[i][2]);
		}

		//처음상태
		stateMachine->setCurrentStateID(STATE_IN);
	}
	void update() {
		switch (stateMachine->getCurrentStateID()) {

		case STATE_IN:
			stateMachine->issueEvent(entrance_market);
			break;
		case STATE_STAND:
			break;
		case STATE_SUCCESS:
			if (!lock) {
				stateMachine->issueEvent(event_success);
				success_count++;
				lock = true;
			}
			break;
		case STATE_FAIL:
			if (!lock) {
				stateMachine->issueEvent(event_fail);
				fail_count++;
				lock = true;
			}
			break;
		case STATE_OUT:
			stateMachine->issueEvent(event_reset);
			lock = false;
			break;
		}
	}
};