<%@ page 	language="java" 
			contentType="text/html; charset=UTF-8"
    		pageEncoding="UTF-8" %>

<%@ taglib	prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>네이버 검색 API</title>
<style>
@font-face {
	font-family: 'Pretendard-Regular';
	src: url('https://cdn.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-Regular.woff') format('woff');
	font-weight: 400;
	font-style: normal;
}

body {
	font-family: 'Pretendard-Regular', sans-serif;
	background-color: #f9f9f9;
	margin: 0;
	padding: 0;
}

.container {
	max-width: 800px;
	margin: 20px auto;
	padding: 20px;
	background-color: #fff;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	border-radius: 8px;
}

.search-container {
	display: flex;
	align-items: center;
	margin-bottom: 20px;
}

#searchInput {
	width: 200px;
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 4px;
	margin-right: 10px;
}

#searchBtn {
	padding: 10px 20px;
	background-color: #4CAF50;
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

#searchBtn:hover {
	background-color: #45a049;
}

#searchResults {
	margin-top: 20px;
}

#map {
	width: 100%;
	height: 400px;
	text-align: center;
	display: flex;
	justify-content: center;
	align-items: center;
	border-radius: 5px;
	overflow: hidden;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.modal {
	display: none;
	position: fixed;
	z-index: 1;
	left: 0;
	top: 0;
	width: 100%;
	height: 100%;
	overflow: auto;
	background-color: rgba(0, 0, 0, 0.4);
	padding-top: 60px;
}

.modal-content {
	background-color: #fefefe;
	margin: 5% auto;
	padding: 20px;
	border-radius: 5px;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.close {
	color: #aaa;
	float: right;
	font-size: 28px;
	font-weight: bold;
}

.close:hover, .close:focus {
	color: black;
	text-decoration: none;
	cursor: pointer;
}

.clicked-marker-info {
	margin-top: 20px;
	padding: 10px;
	background-color: #f2f2f2;
	border: 1px solid #ccc;
	border-radius: 8px;
}

.restaurant-item {
	border: 1px solid #ccc;
	border-radius: 8px;
	padding: 20px;
	margin-bottom: 20px;
	background-color: #fff;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.restaurant-item strong {
	color: #333;
	font-size: 18px;
}

.restaurant-item p {
	margin-top: 10px;
	color: #666;
	font-size: 16px;
}
    </style>
</head>
<body>

	<div class="container">
		<h1>네이버 검색 API</h1>
		<div class="search-container">
			<input type="text" id="searchInput" placeholder="검색어를 입력하세요">
			<button id="searchBtn">검색</button>
		</div>
		<div id="map">검색을 시작해주세요</div>
		<div id="searchResults"></div>
	</div>

	<div id="modal" class="modal">
		<div class="modal-content">
			<span class="close">&times;</span>
			<p id="modalContent">마커 정보</p>
		</div>
	</div>

	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=e803ab993989f8c979039aa3a2a549c9"></script>

    <script>
        $(document).ready(function() {
            $('#searchBtn').click(function() {
                // alert("btn click");
                let query = $("#searchInput").val();
                $.ajax({
                    url : '/api/naver/local/'+query,
                    method : "GET",
                    success : function(data) {
                        // alert(data);
                        displaySearchResults(data);
                        displayMarkers(data);
                    },
                    error : function(xhr, status, error) {
                        console.log('Error', error, status);
                        
                    }
                })
            });
        });

        // 검색결과 출력하는 함수 추가
        function displaySearchResults(restaurants) {
			let searchResultsElement = document.getElementById('searchResults');
			searchResultsElement.innerHTML = ''; // 이전 검색 결과 지우기

			// 검색 결과를 목록으로 변환하여 출력
			restaurants.forEach(function(restaurant) {
				let listItem = document.createElement('div');
                listItem.classList.add('restaurant-item');
				// 각 장소의 이름과 주소 표시
				listItem.innerHTML = '<strong>'
						+ restaurant.title.replace(/<[^>]+>/g, '') // HTML 태그 제거
						+ '</strong><br>' + restaurant.address;
				searchResultsElement.appendChild(listItem);

                listItem.addEventListener('click', function() {
                    moveMapToRestaurantLocation(restaurant) ;
                })
			});
		}

        const imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";
        function displayMarkers(restaurants) {
            const map = new kakao.maps.Map(document.getElementById('map'), {
                center: new kakao.maps.LatLng(restaurants[0].lat, restaurants[0].lng),
                level: 3
            });
            // 모든 장소에 대해 마커 표시
            restaurants.forEach(function(restaurant) {
                const latitude = restaurant.lat; // 경도
                const longitude = restaurant.lng; // 위도
                // 마커 이미지 크기
                const imageSize = new kakao.maps.Size(24, 35);
                // 마커 이미지 생성
                const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
                // 마커 생성
                const marker = new kakao.maps.Marker({
                    map: map, // 지도
                    position: new kakao.maps.LatLng(latitude, longitude), // 위치
                    title: restaurant.title.replace(/<[^>]+>/g, ''), // 이름
                    image: markerImage // 이미지
                });
                // 마커 클릭 이벤트 등록
                kakao.maps.event.addListener(marker, 'click', function() {
                    // 클릭한 마커의 정보를 모달로 표시
                    const modal = document.getElementById('modal');
                    const modalContent = document.getElementById('modalContent');
                    modal.style.display = "block";
                    modalContent.innerHTML = this.getTitle();
                    // 클릭한 마커 정보를 아래에도 표시
                    const clickedMarkerInfo = document.getElementById('clickedMarkerInfo');
                    clickedMarkerInfo.innerHTML = this.getTitle();
                });
            });
        }

        function moveMapToRestaurantLocation(clickedRestaurant) {
            if (clickedRestaurant) { // 클릭한 장소 정보가 존재하는 경우
                const mapContainer = document.getElementById('map'); // 지도를 표시할 영역의 DOM 요소
                const mapOption = { // 지도 옵션 설정
                    center: new kakao.maps.LatLng(clickedRestaurant.lat, clickedRestaurant.lng), // 지도의 중심좌표 설정
                    level: 3 // 지도 확대 레벨 설정
                };

                // 지도 객체 생성
                const map = new kakao.maps.Map(mapContainer, mapOption);

                // 클릭한 장소 위치에 마커 생성
                const marker = new kakao.maps.Marker({
                    map: map, // 마커를 표시할 지도 객체 설정
                    position: new kakao.maps.LatLng(clickedRestaurant.lat, clickedRestaurant.lng), // 마커의 위치 설정
                    title: clickedRestaurant.title.replace(/<[^>]+>/g, '') // 마커에 표시될 타이틀 설정 (HTML 태그 제거)
                });

                // 마커를 클릭했을 때 모달을 표시하고 내용을 설정하는 이벤트 리스너 등록
                kakao.maps.event.addListener(marker, 'click', function() {
                    const modal = $('#modal'); // 모달 요소 선택
                    const modalContent = $('#modalContent'); // 모달 내용 요소 선택
                    modal.css("display", "block"); // 모달 표시
                    modalContent.html(this.getTitle()); // 모달 내용 설정 (마커의 타이틀로 설정)

                    const clickedMarkerInfo = $('#clickedMarkerInfo'); // 클릭한 마커 정보 요소 선택
                    clickedMarkerInfo.html(this.getTitle()); // 클릭한 마커의 타이틀 표시
                });
            }}

            // 모달 닫기
            const closeBtn = document.getElementsByClassName("close")[0];
            closeBtn.onclick = function() {
                const modal = document.getElementById('modal');
                modal.style.display = "none";
            }

    </script>

</body>
</html>