package com.agebra.boonbaebackend.controller;

import com.agebra.boonbaebackend.domain.QnAType;
import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.dto.QnADto;
import com.agebra.boonbaebackend.service.QnAService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "QnAController", description = "QnA 관련 컨트롤러입니다")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/qna")
public class QnAController {
    private final QnAService qnaService;
    @PostMapping() //QnA 작성
    public ResponseEntity<Void> writeQnA(@RequestBody QnADto.Request dto, @AuthenticationPrincipal Users user){
        qnaService.write(dto,user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping("/{qna_pk}") //QnA 수정 - 글 쓴사람 전용
    public ResponseEntity<Void> updateQnA(@PathVariable Long qna_pk, @RequestBody QnADto.Request dto, @AuthenticationPrincipal Users user){
        qnaService.update_QnA(qna_pk,dto,user);

        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{qna_pk}") // qna 삭제
    public ResponseEntity<Void> deleteQnA(@PathVariable Long qna_pk, @AuthenticationPrincipal Users user){
        qnaService.delete(qna_pk, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/") //QnA 페이지 - 전체 다 불러오게 해달라고 하셨음
    public ResponseEntity<Map<String, List>> findAllQnA(
      @RequestParam(value = "category",required = false) QnAType category
    ){
        List<QnADto.Response_AllQnA> dto = qnaService.all_QnA(category);

        Map<String, List> map = new HashMap<>();
        map.put("list", dto);

        return ResponseEntity.ok(map);
    }

//    @GetMapping("/") //QnA 페이지
//    public ResponseEntity<List<QnADto.Response_AllQnA>> findAllQnA(
//      @RequestParam(value = "category",required = false) QnAType category,
//      @RequestParam(value = "size",required = false, defaultValue = "10") int size,
//      @RequestParam(value = "page",required = false, defaultValue = "0") int page
//    ){
//        //direction = Sort.Direction.DESC
//        Pageable pageable = PageRequest.of(page,size, Sort.Direction.DESC, "createAt");
//
//        List<QnADto.Response_AllQnA> dto = qnaService.page_QnA(pageable,category);
//        return ResponseEntity.ok().body(dto);
//    }

    @GetMapping("/{qna_pk}") //QnA 상세정보
    public ResponseEntity<QnADto.Response_oneQnA> findOneQnA(@PathVariable Long qna_pk){
        QnADto.Response_oneQnA dto = qnaService.one_QnA(qna_pk);

        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/{qna_pk}/reply") //답변 작성 - 관리자 전용
    public ResponseEntity<Void> writeReply(@PathVariable Long qna_pk,@RequestBody Map<String, String> map){
        String reply = map.get("content");
        if (reply == null)
            throw new InputMismatchException("content값은 필수입니다");

        qnaService.reply_QnA(qna_pk,reply);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{qna_pk}/reply") //답변 수정 - 관리자 전용
    public ResponseEntity<Void> updateReply(@PathVariable Long qna_pk,@RequestBody Map<String, String> map){
        String reply = map.get("content");
        if (reply == null)
            throw new InputMismatchException("content값은 필수입니다");

        qnaService.update_Reply(qna_pk,reply);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{qna_pk}/reply") //답변 작성 - 관리자 전용
    public ResponseEntity<Void> deleteReply(@PathVariable Long qna_pk){
        qnaService.delete_Reply(qna_pk);
        return ResponseEntity.ok().build();
    }
}