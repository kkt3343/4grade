#pragma once
#include <windows.h>
#include <map>
#include <functional>

#define UNDEFINED		0

struct State
{
	DWORD stateID;
	std::map<DWORD, DWORD> transitions; //(key,value)=(inputEvent,outputStateID)

	State(DWORD _stateID) { stateID = _stateID; }
	~State() { transitions.clear(); }
	DWORD getStateID() { return stateID; }
	DWORD getNumTransitions() { return transitions.size(); }
	void addTransition(DWORD inputEvent, DWORD outputStateID) { transitions[inputEvent] = outputStateID; }
	void deleteTransition(DWORD inputEvent) { transitions.erase(inputEvent); }
	DWORD getOutputStateID(DWORD inputEvent)
	{
		// �Է��̺�Ʈ�� ���� ���� ���ǿ� �ش��ϴ� �������� ���¸� ã�Ƴ���.
		std::map<DWORD, DWORD>::iterator iter = transitions.find(inputEvent);
		if (iter == transitions.end()) { return UNDEFINED; } // ��ϵ� ���̰� ����.
		else return transitions[inputEvent];
	}
};


class FiniteStateMachine
{
private:
	std::map<DWORD, State*> states; //(key,value)=(stateID, state object)
	DWORD currentStateID;

public:
	FiniteStateMachine();
	~FiniteStateMachine();

	void addStateTransition(DWORD stateID, DWORD inputEvent, DWORD outputStateID);
	void deleteTransition(DWORD stateID, DWORD inputEvent);

	void issueEvent(int inputEvent);
	DWORD getCurrentStateID() { return currentStateID; }
	void setCurrentStateID(DWORD stateID);

private:

};
