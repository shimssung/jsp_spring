package com.example.demo.api.ctrl;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.api.domain.SearchDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.net.URI;


@RestController
@RequestMapping("/api")
public class ApiController {
    // Value는 springframework 꺼 여야함
    // 해줘야 application.properties 에 저장한걸 가져올 수 있음
    @Value("${naver.client-id}")
    private String client ;

    @Value("${naver.secret}")
    private String secret ;

    @GetMapping("/naver/local/{name}")
    public ResponseEntity<List<SearchDTO>> local(@PathVariable("name") String name) {
        System.out.println("debug >>> user endpont : /api/naver/local/{name}");
        System.out.println("debug >>> params = " + name);
        List<SearchDTO> list = search(name);
        return new ResponseEntity<>(list, HttpStatus.OK) ;
    }

    // api 이용하여 장소를 검색하고 반환하는 역할
    // 한글은 인코딩이 필수!!
    // ObjectMapper(json -> dto)
    public List<SearchDTO> search(String query) {

        // 리턴타입에 맞게 리턴하기위해 List를 try바깥으로 빼줌
        List<SearchDTO> list      = new ArrayList<>();

        try {
            ByteBuffer buffer = StandardCharsets.UTF_8.encode(query);
            String encode = StandardCharsets.UTF_8.decode(buffer).toString();
            System.out.println("debug >>> query encode = " + encode);

            // 인코딩된 코드를가지고 통신 (검색 URL 생성)
            URI uri = UriComponentsBuilder
                        .fromUriString("https://openapi.naver.com")
                        .path("/v1/search/local")
                        .queryParam("query", encode)
                        .queryParam("display", 20)
                        .queryParam("start", 1)
                        .queryParam("sort", "random")
                        .encode().build().toUri();
            System.out.println("debug >>> 검색 URL 생성완료!!");

            // 요청전달 RestTemplate
            // RestTemplate이 필요한 이유: Header에 클라이언트 아이디와 시크릿을 포함해서 보내야하기 때문
            // 아이디와 시크릿을 담아서 json으로 보내려면 RestTemplate이 필요함
            RestTemplate restTemplate = new RestTemplate();

            RequestEntity<Void> request = RequestEntity
                                            .get(uri)
                                            .header("X-Naver-Client-Id", client)
                                            .header("X-Naver-Client-Secret", secret)
                                            .build();
            System.out.println("debug >>> 요청전달 RestTemplate 완료!!");

            // 서버로부터 내려받은 json을 ResponseEntity가 받음
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            System.out.println("debug >>> json 형식의 응답 완료!!");
            // System.out.println("json - " );
            // System.out.println(response.getBody());

            // 서버로부터 받은 json형식을 문자열로 치환
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode         rootNode = objectMapper.readTree(response.getBody());
            JsonNode         items    = rootNode.path("items");
            
            for(JsonNode item : items) {
                // System.out.println(item.get("title"));

                // 만들어진 데이터를 DTO에 담고 DTO를 list에 담는 역할
                // DTO에 담는다는건 json화 시킨다는 것.
                SearchDTO dto = new SearchDTO();
                dto.setTitle(item.get("title").asText());
                dto.setAddress(item.get("address").asText());
                // dto.setLat(item.get("mapx").asText());
                // dto.setLng(item.get("mapy").asText());

                // 좌표 데이터를 올바른 형식으로 변환하여 Kakao 지도 API에서 사용할 수 있도록 합니다. 
                //Kakao 지도 API는 실제 위도와 경도 값을 필요로 하므로, 스케일링된 좌표 데이터를 1e7로 나눠 변환하는 과정이 필요
                dto.setLat(String.valueOf( Double.parseDouble( item.get("mapy").asText() ) / 1e7 ));
                dto.setLng(String.valueOf( Double.parseDouble( item.get("mapx").asText() ) / 1e7 ));
                list.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
}
