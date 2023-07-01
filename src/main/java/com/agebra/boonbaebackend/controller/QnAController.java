package com.agebra.boonbaebackend.controller;

import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.dto.QnADto;
import com.agebra.boonbaebackend.service.QnAService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.ok().build();
    }
    @GetMapping("/") //QnA 페이지 
    public ResponseEntity<List<QnADto.Response_AllQnA>> findAllQnA(
            @RequestParam(value = "category",required = false, defaultValue = "") String category,
            @RequestParam(value = "size",required = false, defaultValue = "10") int size,
            @RequestParam(value = "page",required = false, defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page,size);
        List<QnADto.Response_AllQnA> dto = qnaService.all_QnA(pageable,category);
        return ResponseEntity.ok().body(dto);
    }
    @GetMapping("/{qnaPk}") //QnA 상세정보
    public ResponseEntity<QnADto.Response_oneQnA> findOneQnA(@PathVariable Long qnaPk){
        QnADto.Response_oneQnA dto = qnaService.one_QnA(qnaPk);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/{qnaPk}") //QnA 수정
    public ResponseEntity<Void> updateQnA(@PathVariable Long qnaPk, @RequestBody QnADto.Request dto, @AuthenticationPrincipal Users user){
        qnaService.update_QnA(qnaPk,dto,user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{qnaPk}") // qna 삭제
    public ResponseEntity<Void> deleteQnA(@PathVariable Long qnaPk){
        qnaService.delete(qnaPk);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{qnaPk}/reply") //답변 작성
    public ResponseEntity<Void> writeReply(@PathVariable Long qnaPk,@RequestBody String reply){
        qnaService.reply_QnA(qnaPk,reply);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{qnaPk}/reply") //답변 수정
    public ResponseEntity<Void> updateReply(@PathVariable Long qnaPk,@RequestBody String reply){
        qnaService.update_Reply(qnaPk,reply);
        return ResponseEntity.ok().build();
    }
}
