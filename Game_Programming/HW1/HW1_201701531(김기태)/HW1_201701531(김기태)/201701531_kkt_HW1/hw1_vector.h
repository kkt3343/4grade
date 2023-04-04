#pragma once

#include "Resource.h"
#include <iostream>

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
	if (p1.score > p2.score) {
		return p1.score < p2.score;
	}
	else if (p1.score < p2.score) {
		return p1.score < p2.score;
	}
	else {
		return p1.score < p2.score;
	}
}