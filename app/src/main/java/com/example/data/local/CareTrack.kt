package com.example.data.local

enum class CareTrack(
  val code: String,
  val title: String,
  val shortTitle: String,
  val summary: String,
  val badgeLabel: String,
  val coreAdvice: String,
  val targetDescription: String
) {
  TRACK_A(
    code = "A",
    title = "출산·영양 안심 트랙",
    shortTitle = "출산·영양 안심",
    summary = "약물 없는 자연 회복 안내 및 심리적 안심 코칭",
    badgeLabel = "경로 A • 안심 회복",
    coreAdvice = "출산 2~4개월 차의 대량 탈락(휴지기 탈모)은 호르몬 급감에 따른 자연스러운 회복 반응입니다. 약물 치료는 불필요하며, 페리틴(저장철)·단백질 영양 균형과 수면 관리가 핵심입니다.",
    targetDescription = "출산 1년 미만 산모, 모유 수유 중이거나 급격한 다이어트/영양 결핍군"
  ),
  TRACK_B(
    code = "B",
    title = "가르마 집중 완주 트랙",
    shortTitle = "가르마 집중 완주",
    summary = "1차 표준 외용제 12개월 완주 & 쉐딩 데스밸리 방어",
    badgeLabel = "경로 B • 12개월 완주",
    coreAdvice = "가르마 선 연모화는 장기 관리가 필요한 표준 관리 영역입니다. 도포 2~8주 차에 겪는 일시적 털 빠짐(쉐딩)은 굵은 새 모발이 올라오는 정상 반응이므로 안심하고 12개월을 완주하세요.",
    targetDescription = "가르마 선 비침 및 모발 가늘어짐이 1년 이상 지속된 만성 탈모군"
  ),
  TRACK_C(
    code = "C",
    title = "전문의 진료 연계 트랙",
    shortTitle = "전문의 진료 연계",
    summary = "홈케어 잠금 및 신속한 피부과 전문의 상담 준비 지원",
    badgeLabel = "경로 C • 병원 연계",
    coreAdvice = "동전 모양 둥근 탈모반, 붉은 염증·통증, 모공 소실(흉터) 의심 신호는 샴푸나 홈케어로 지체할 경우 모낭이 영구 손상될 수 있습니다. 모디파이 진료 요약 리포트를 지참하여 즉시 피부과 전문의를 방문하세요.",
    targetDescription = "원형 탈모반, 급격한 두피 통증/진물, 모공이 닫힌 흉터성 탈모 의심군"
  );

  companion object {
    fun fromCode(code: String): CareTrack {
      return entries.find { it.code == code } ?: TRACK_B
    }
  }
}
