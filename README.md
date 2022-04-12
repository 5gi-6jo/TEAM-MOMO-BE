# 모두모여(Momo) Back-end

## - Websocket을 활용한 실시간 위치 공유 및 스케쥴 관리 서비스

### 1. 프로젝트 기간 (5주)

2022.03.04(금) ~ 2022.04.09(토)

### 2. 팀 구성

역할 |이름 | Github | 포지션 
-|-|-|- 
부팀장 | 유진환 | https://github.com/JinhwanU | Back-end 
팀원 | 김재훈 | https://github.com/HoduUlmu | Back-end 
팀장 | 장윤철 | https://github.com/name8965 | Front-end 
팀원 | 정호상 | https://github.com/5aro | Front-end
팀원 | 이지현 | | Designer
팀원 | 신소연 | | Designer
### 3. 모두모여란?

모두모여 서비스 바로가기 : https://modumoyeo.com/



<details>
<summary>서비스 소개</summary>
<div markdown="1">

![Instagram post - 1](https://user-images.githubusercontent.com/96904426/163006452-ffe9247f-d09d-4ab7-a5ea-613dd62cb2f4.png)
![Instagram post - 2](https://user-images.githubusercontent.com/96904426/163006459-0afe4e90-c36e-41e2-8b94-fc4a6f556141.png)
![Instagram post - 3](https://user-images.githubusercontent.com/96904426/163006463-ddbf5f39-7605-42df-83ad-c8ac45a40224.png)
![Instagram post - 4](https://user-images.githubusercontent.com/96904426/163006468-79af106f-6476-412a-b140-ae9681a03a53.png)
</div>
</details>

### 4. 기술 스택

<img src="https://img.shields.io/badge/MYSQL-4479A1?style=for-the-badge&logo=MYSQL&logoColor=white"><img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white">
<img src="https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">
<img src="https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=Firebase&logoColor=white">
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
<img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white">
<img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white">
<img src="https://img.shields.io/badge/GitHubActions-2088FF?style=for-the-badge&logo=GitHubActions&logoColor=white">

### 5. 프로젝트 세부내용

<details>
<summary><b>ERD</b></summary>
<div markdown="1">       

![image](https://user-images.githubusercontent.com/96904426/163010210-9c0e64a5-6ec5-4baf-a9ae-ebe9a1fb6672.png)


</div>
</details>
<hr>
<details>
<summary><b>서비스 아키텍쳐</b></summary>
<div markdown="1">       

![1](https://user-images.githubusercontent.com/96904426/163008538-17a5043f-f048-451d-9f67-e51afa3bb475.png)

</div>
</details>

### 6. 문제 해결 과정

<details>
<summary><b>게스트 유저 관리 전략</b></summary>
<div markdown="1">       

<b><h3>문제 상황</h3></b>
유저 편의성 및 접근성을 위해 Guest User 기능의 필요성을 인식<br>
이에, Guest User 데이터를 어떻게 관리하는 것이 좋을지에 대한 고민

<b><h3>해결 방안</h3></b>

1. Redis 사용<br>
   클라이언트에게 요청을 받으면 해당 정보를 Redis에 저장하고 저장된 데이터는 일정 시간이 지난 후 만료되도록 설계<br>
   MySQL DB에서 게스트 유저 정보를 생성 및 삭제 하는 것보다 더 빠르고 적은 비용으로 관리할 수 있다고 판단하였다<br>
   다만, I/O가 많이 발생하는 데이터가 아니기 때문에 Redis에 적합하지 않고 게스트 유저 데이터를 활용할 수 없다는 문제가 존재했다 <hr>
2. Guest User Pool을 생성하여 클라이언트가 요청 시 임의의 Guest Id 할당(MySQL) <br>
   게임 개발에서 주로 사용하는 Object pooling 기법과 thread pool에서 착안<br>
   관리 측면에서 문제가 발생할 여지가 많았고, 이전 방법과 마찬가지로 게스트 user 데이터를 추후 활용할 수 없다는 문제가 존재했다<hr>
3. 클라이언트가 요청 시 생성하여 관리하는 일반적인 방법(MySQL)<br>
   redis를 사용하는 것보다 성능면에서 조금 부족하지만 게스트 유저 데이터를 삭제하지 않고 쌓아두어 추후 활용할 수 있으며,2번 방식보다 데이터 관리가 용이하다는 장점이 존재했다<br>

<b><h3>의견 결정</h3></b>
위 세 가지 방법들을 종합하여 고민한 결과 게스트 유저 기능에서는 I/O 성능보다 데이터 관리와 활용성이 더 중요하다고 판단하여 세번째 방법을 채택하기로 결정

</div>
</details>

<details>
<summary><b>게스트 유저 테이블 설계</b></summary>
<div markdown="1">       

<b><h3>문제 상황</h3></b>
게스트 유저 관리 전략에 대한 의견 결정으로 MySQL을 사용하기로 하였으나 테이블 설계에서 어려움을 겪음<br>
테이블 설계에 주로 사용했던 연관 관계 매핑 기법으로 문제 해결하려 했으나 테이블의 복잡도가 상승하여 다른 방안을 모색하기로 함
<b><h3>해결 방안</h3></b>

상속 관계 매핑에 대해서 알게되었고, 그 중 두 가지 방법 중 적합한 것을 선택하기로 했다.
1. 조인 테이블 전략<br>
   테이블의 정규화 및 외래키 참조 무결성 제약 조건에 위배되지 않는다는 장점이 있지만 조인으로 인한 성능 저하가 예상됨<hr>
   
2. 단일 테이블 전략<br>
   단일 테이블로 관리하기 때문에 조인 테이블 전략에 비해 더 나은 조회 성능을 가지고 있다<br>
   단점으로는 자식 엔티티가 매핑한 컬럼은 모두 null을 허용해야 한다

<b><h3>의견 결정</h3></b>
유저 정보에 null을 적용하기는 불가능하다고 판단하여 조인 테이블 전략을 채택하였다<br>
그 결과 다음과 같이 간단한 테이블이 설계되었다 (기존에는 4~5개 이상의 테이블이 생성되고 연관관계가 복잡했음)<br>
![image](https://user-images.githubusercontent.com/96904426/163033508-098060da-8bbc-4fdf-a25a-33510ac50461.png)
</div>
</details>

<details>
<summary><b>Cache 적용하여 주요 API 성능 개선(작성 예정)</b></summary>
<div markdown="1">       

<b><h3>문제 상황</h3></b>
<b><h3>해결 방안</h3></b>
<b><h3>의견 결정</h3></b>
</div>
</details>


<details>
<summary><b>Thread Pool 활용한 Push Message 전송 성능 개선(작성 예정)</b></summary>
<div markdown="1">       

<b><h3>문제 상황</h3></b>
<b><h3>해결 방안</h3></b>
<b><h3>의견 결정</h3></b>
</div>
</details>


<details>
<summary><b>연관 관계 테이블의 N+1 문제 해결(작성 예정)</b></summary>
<div markdown="1">       

<b><h3>문제 상황</h3></b>
<b><h3>해결 방안</h3></b>
<b><h3>의견 결정</h3></b>
</div>
</details>




<details>
<summary><b>Code Refactoring(AOP 적용)(작성 예정)</b></summary>
<div markdown="1">

<b><h3>문제 상황</h3></b>
<b><h3>해결 방안</h3></b>
<b><h3>의견 결정</h3></b>
</div>
</details>

