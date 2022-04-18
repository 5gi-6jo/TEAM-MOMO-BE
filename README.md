# 모두모여(Momo) Back-end

## - Websocket을 활용한 실시간 위치 공유 및 스케쥴 관리 서비스

### 1. 프로젝트 기간 (5주)

2022.03.04(금) ~ 2022.04.08(금)
<br>

### 2. 팀 구성

역할 |이름 | Github | 포지션 
-|-|-|- 
부팀장 | 유진환 | https://github.com/JinhwanU | Back-end 
팀원 | 김재훈 | https://github.com/HoduUlmu | Back-end 
팀장 | 장윤철 | https://github.com/name8965 | Front-end 
팀원 | 정호상 | https://github.com/5aro | Front-end
팀원 | 이지현 | | Designer
팀원 | 신소연 | | Designer
<br>

### 3. 모두모여란?

모두모여 서비스 바로가기 : https://modumoyeo.com/



<details>
   <summary><b>서비스 소개</b></summary>
<div markdown="1">

![Instagram post - 1](https://user-images.githubusercontent.com/96904426/163006452-ffe9247f-d09d-4ab7-a5ea-613dd62cb2f4.png)
![Instagram post - 2](https://user-images.githubusercontent.com/96904426/163006459-0afe4e90-c36e-41e2-8b94-fc4a6f556141.png)
![Instagram post - 3](https://user-images.githubusercontent.com/96904426/163006463-ddbf5f39-7605-42df-83ad-c8ac45a40224.png)
![Instagram post - 4](https://user-images.githubusercontent.com/96904426/163006468-79af106f-6476-412a-b140-ae9681a03a53.png)
</div>
</details>
<br>

### 4. 기술 스택

<img src="https://img.shields.io/badge/MYSQL-4479A1?style=for-the-badge&logo=MYSQL&logoColor=white"><img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white">
<img src="https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">
<img src="https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=Firebase&logoColor=white">
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
<img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white">
<img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white">
<img src="https://img.shields.io/badge/GitHubActions-2088FF?style=for-the-badge&logo=GitHubActions&logoColor=white">
<br><br>

### 5. 프로젝트 세부내용

<details>
<summary><b>ERD</b></summary>
<div markdown="1">       

![image](https://user-images.githubusercontent.com/96904426/163010210-9c0e64a5-6ec5-4baf-a9ae-ebe9a1fb6672.png)

</div>
</details>

<details>
<summary><b>서비스 아키텍쳐</b></summary>
<div markdown="1">       

![1](https://user-images.githubusercontent.com/96904426/163008538-17a5043f-f048-451d-9f67-e51afa3bb475.png)

</div>
</details>
<br>

### 6. 문제 해결 과정

<details>
<summary><b>게스트 유저 관리 전략</b></summary>
<div markdown="1">       

<b><h3>⚠️ 문제 상황</h3></b>
유저 편의성 및 접근성을 위해 Guest User 기능의 필요성을 인식<br>
이에, Guest User 데이터를 어떻게 관리하는 것이 좋을지에 대한 고민

<b><h3>💬 해결 방안</h3></b>

1. Redis 사용<br>
   클라이언트에게 요청을 받으면 해당 정보를 Redis에 저장하고 저장된 데이터는 일정 시간이 지난 후 만료되도록 설계<br>
   MySQL DB에서 게스트 유저 정보를 생성 및 삭제 하는 것보다 더 빠르고 적은 비용으로 관리할 수 있다고 판단하였다<br>
   다만, I/O가 많이 발생하는 데이터가 아니기 때문에 Redis에 적합하지 않고 게스트 유저 데이터를 활용할 수 없다는 문제가 존재했다 <br>
2. Guest User Pool을 생성하여 클라이언트가 요청 시 임의의 Guest Id 할당(MySQL) <br>
   게임 개발에서 주로 사용하는 Object pooling 기법과 thread pool에서 착안<br>
   관리 측면에서 문제가 발생할 여지가 많았고, 이전 방법과 마찬가지로 게스트 user 데이터를 추후 활용할 수 없다는 문제가 존재했다<br>
3. 클라이언트가 요청 시 생성하여 관리하는 일반적인 방법(MySQL)<br>
   redis를 사용하는 것보다 성능면에서 조금 부족하지만 게스트 유저 데이터를 삭제하지 않고 쌓아두어 추후 활용할 수 있으며,2번 방식보다 데이터 관리가 용이하다는 장점이 존재했다<br>

<b><h3>✅ 의견 결정</h3></b>
위 세 가지 방법들을 종합하여 고민한 결과 게스트 유저 기능에서는 I/O 성능보다 데이터 관리와 활용성이 더 중요하다고 판단하여 세번째 방법을 채택하기로 결정
<br><br><hr>
   
</div>
</details>

<details>
<summary><b>게스트 유저 테이블 설계</b></summary>
<div markdown="1">       

<b><h3>⚠️ 문제 상황</h3></b>
게스트 유저 관리 전략에 대한 의견 결정으로 MySQL을 사용하기로 하였으나 테이블 설계에서 어려움을 겪음<br>
테이블 설계에 주로 사용했던 연관 관계 매핑 기법으로 문제 해결하려 했으나 테이블의 복잡도가 상승하여 다른 방안을 모색하기로 함
<b><h3>💬 해결 방안</h3></b>

상속 관계 매핑에 대해서 알게되었고, 그 중 두 가지 방법 중 적합한 것을 선택하기로 했다.
1. 조인 테이블 전략<br>
   테이블의 정규화 및 외래키 참조 무결성 제약 조건에 위배되지 않는다는 장점이 있지만 조인으로 인한 성능 저하가 예상됨<br>
   
2. 단일 테이블 전략<br>
   단일 테이블로 관리하기 때문에 조인 테이블 전략에 비해 더 나은 조회 성능을 가지고 있다<br>
   단점으로는 자식 엔티티가 매핑한 컬럼은 모두 null을 허용해야 한다

<b><h3>✅ 의견 결정</h3></b>
유저 정보에 null을 적용하기는 불가능하다고 판단하여 조인 테이블 전략을 채택하였다<br>
그 결과 다음과 같이 간단한 테이블이 설계되었다 (기존에는 4~5개 이상의 테이블이 생성되고 연관관계가 복잡했음)<br>
![image](https://user-images.githubusercontent.com/96904426/163033508-098060da-8bbc-4fdf-a25a-33510ac50461.png)
   
<br><br><hr>
</div>
</details>

<details>
<summary><b>Cache 적용하여 API 성능 개선</b></summary>
<div markdown="1">       

<b><h3>⚠️ 문제 상황</h3></b>
   EC2 서버 업그레이드 후(t2.micro->t3.medium) nGrinder를 사용한 스트레스 테스트 진행 결과,<br>
   Vuser 500명 기준 CPU 사용률 80% 기록하여 성능 개선이 필요함을 인지하였다<br>
   Query 개선과 Cache을 적용하여 성능 개선을 기대해 볼 수 있었는데,<br> 
   JPQL 및 QueryDSL에 대한 충분한 공부를 없이 Query개선을 시도하기보다는 (프로젝트 마감 기한이 정해져 있기 때문에 시간적 여유가 없다)<br>
   Cache를 적용하여 성능 개선을 노려보기로 하였다<br>
   
   
<b><h3>💬 해결 방안</h3></b>
   <b>Local Cahce</b><br>
   Local Cache는 서버 내부 저장소에 Cache 데이터를 저장하는 방식으로, 속도는 빠르지만 서버 간의 데이터 공유가 안된다는 단점이 있다.<br>
   예를 들어, 사용자가 같은 리소스에 대한 요청을 반복해서 보내더라도, A 서버에서는 이전 데이터를, B 서버에서는 최신 데이터를 반환하여 각 Cache가 서로 다른 상태를 가질 수도 있다.<br> 
   즉, 일관성 문제가 발생할 수 있다.
   
   <b>Global Cache</b><br>
   Global Cache는 서버 내부 저장소가 아닌 별도의 Cache 서버를 두어 서버에서 Cache 서버를 참조하는 것이다.<br>
   Cache 데이터를 얻으려 할 때 마다 네트워크 트래픽이 발생하기 때문에 Local Cache보다 속도는 느리지만,<br>
   서버간 데이터를 쉽게 공유할 수 있기 때문에 Local Cache의 정합성 문제와 중복된 캐시 데이터로 인한 서버 자원 낭비 등 문제점을 해결할 수 있다.<br>
   
<b><h3>✅ 의견 결정</h3></b>
   현재 프로젝트가 단일 서버 환경이었기 때문에 Local Cache를 적용하려고 하였고 이것이 정답이라고 생각하였다<br>
   하지만, 프로젝트에서 이미 Redis를 사용중이며,<br>
   실무에서는 다중 서버 환경에서 작업을 많이 할 것이라고 짐작하여 공부 목적으로 Global Cache를 적용하였다<br><br>
   <b>Cache 적용 후 성능 개선 결과</b> https://github.com/5gi-6jo/TEAM-MOMO-BE/issues/72#issue-1193479148
   
<br><br><hr>
</div>
</details>


<details>
<summary><b>Thread Pool 활용한 Push Message 전송 성능 개선(작성 예정)</b></summary>
<div markdown="1">       

<b><h3>문제 상황</h3></b>
<b><h3>해결 방안</h3></b>
<b><h3>의견 결정</h3></b>
   
<br><br><hr>
</div>
</details>


<details>
<summary><b>연관 관계 테이블의 N+1 문제 해결(작성 예정)</b></summary>
<div markdown="1">       

<b><h3>문제 상황</h3></b>
<b><h3>해결 방안</h3></b>
<b><h3>의견 결정</h3></b>
   
<br><br><hr>
</div>
</details>



<details>
<summary><b>Code Refactoring(AOP 적용)</b></summary>
<div markdown="1">

<b><h3>문제 상황</h3></b>
API를 호출한 클라이언트의 `로그인 여부 판단`과 `Request Dto`에 대한 검증과 같은 주 기능이 아닌 `부가 기능`이 여러 컨트롤러의 `API에서 반복적인 코드`로 적용되어 있음
<b><h3>해결 방안</h3></b>
1. 포인트컷 지시자로 execution을 이용해 적용할 메소드를 지정
2. 포인트컷 지시자로 @annotation을 이용해 적용할 메소드를 지정

### ✅ 의견 결정
- `execution`을 이용하면 메소드만 보고는 dto 검증과 로그인 판단 여부에 대한 처리가 이루어지는지 알 수 없음.
- 해당 부가 기능들은 직관적으로 `동작하고 있다고 나타낼 필요`가 있다고 판단.
<aside>
💡 annotation을 생성한 뒤 해당 annotation을 pointcut으로 지시해 aop로 처리함.
annotation을 pointcut으로 지시한 이유는 적용된 메소드에 어떤 부가 동작들이 처리되고 있는지를 직관적으로 알 수 있도록 하기 위해서임.

</aside>
<br><br><hr>
</div>
</details>


<details>
<summary><b>CI/CD 적용</b></summary>
<div markdown="1">       

### 🔍 도입 이유

`배포 자동화`를 통해 효율적인 협업 및 작업 환경을 구축하기 위함

### ⚠️ 문제 상황

`Front-end와 협업` 시 코드 배포를 해야하는 상황이 빈번히 발생

filezila를 통한 수동 배포와 배포 이후 에러 확인되어 재배포하는 일이 잦아짐에 따라 `배포에 많은 시간이 소요`됨

### 🧭 해결 방안

1. Jenkins
2. Github Actions

### 💬 의견 조율

- `Jenkins`를 사용하기 위해선 `서버 설치`가 필요
- `Jenkins`는 `Docker 환경`에서 실행하는 것이 좋음(호환성 issue)
- `Github Actions`는 별다른 설치 및 `복잡한 절차 없이 사용` 가능하다
- `팀원 한명의 이탈`로 인해 개발 인프라에 많은 시간을 할애할 수 없다

### ✅ 의견 결정

<aside>
💡 처음에는 Jenkins를 우선순위로 두었지만, 시간적 제약(서버 설치, Docker 환경 구성)으로 인해 Github Actions를 사용하기로 결정

</aside>
   
<br><br><hr>
</div>
</details>

<details>
<summary><b>Refresh Token 전략</b></summary>
<div markdown="1">       

### 🔍 도입 이유

`refresh token`과 `access token`의 보안 강화

### ⚠️ 문제 상황

refresh token과 access token 모두 header에 담아 클라이언트의 local storage에 저장할 경우 `XSS 공격에 취약`해짐

### 🧭 해결 방안

1. refresh token은 http-only cookie에 access token은 헤더에 담아 보내기
2. 실제 refresh token 값은 db에 저장 후 해당 index만 http-only 쿠키에 저장

### 💬 의견 조율

- access token에 `최소한의 유저 정보`만이 들어가야함(sub: email -> db id로 변경)

### ✅ 의견 결정

<aside>
💡 access token은 해당 유저의 db id값을 subject로, 권한(user,guest)을 claim으로 가지며 header에 실림. refresh token은 유저 정보는 가지지 않으며 http-only cookie에 저장.

</aside>
   
<br><br><hr>
</div>
</details>
