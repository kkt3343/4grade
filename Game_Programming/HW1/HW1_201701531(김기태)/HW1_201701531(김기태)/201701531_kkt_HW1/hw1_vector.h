#pragma once

#include "Resource.h"
#include <iostream>

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