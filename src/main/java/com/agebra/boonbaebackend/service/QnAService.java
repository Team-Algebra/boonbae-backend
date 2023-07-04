package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.*;
import com.agebra.boonbaebackend.dto.QnADto;
import com.agebra.boonbaebackend.exception.ForbiddenException;
import com.agebra.boonbaebackend.exception.NotFoundException;
import com.agebra.boonbaebackend.repository.QnARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QnAService {
    private final QnARepository qnaRepository;
    public QnA write(QnADto.Request dto, Users user){
        QnA qna = QnA.makeQnA(
          user,dto.getQnaType(),dto.getTitle(),dto.getDescription());
        QnA save = qnaRepository.save(qna);
        return save;
    }
    public void update_QnA(Long QnA_pk,QnADto.Request dto, Users user){
        QnA qna = qnaRepository.findById(QnA_pk)
          .orElseThrow(() -> new NotFoundException("일치하는 QnA가 없습니다"));

        if(qna.getUser().getPk() == user.getPk()) {
            qna.editQnA(dto.getQnaType(), dto.getTitle(), dto.getDescription());
        }else{
            throw new ForbiddenException("다른 유저가 작성한 글입니다 해당 유저 닉네임:"+qna.getUser().getNickname());
        }
    }

    public void delete(Long QnA_pk, Users user) throws ForbiddenException {
        QnA qna = qnaRepository.findById(QnA_pk)
          .orElseThrow(() -> new NotFoundException("일치하는 QnA가 없습니다"));

        Users qnaUser = qna.getUser();

        //권한없는 user 처리
        if (user.getRole() != UserRole.ADMIN && user.getPk() != qnaUser.getPk()) {
            throw new ForbiddenException("권한이 없는 사용자입니다");
        }

        qnaRepository.delete(qna);
    }

    @Transactional(readOnly = true)
    public QnADto.Response_oneQnA one_QnA(Long QnA_pk){
        QnA qna =qnaRepository.findById(QnA_pk)
          .orElseThrow(() -> new NotFoundException("일치하는 QnA가 없습니다."));

        String status;
        int isReply;
        if(qna.getReplyText()==null) {
            status = "waiting";
            isReply=0;
        } else {
            status = "answered";
            isReply=1;
        }

        //user가 탈퇴하면 null로 표시
        Users qnaUser = qna.getUser();
        String username = null;
        if (qnaUser != null)
            username = qnaUser.getUsername();

        QnADto.Response_oneQnA dto = QnADto.Response_oneQnA.builder()
          .qnaType(qna.getQnaType())
          .status(status)
          .isReply(isReply)
          .replyText(qna.getReplyText())
          .title(qna.getTitle())
          .userName(username)
          .createAt(qna.getCreateAt())
          .description(qna.getDescriptions())
          .build();
        return dto;
    }
   /* @Transactional(readOnly = true)
    public List<QnADto.Response_AllQnA> page_QnA(Pageable pageable, QnAType category){
        List<QnA> qnaList;

        if (category == null)
            qnaList = qnaRepository.findAll(pageable).getContent();
        else
            qnaList = qnaRepository.findByQnaType(category, pageable).getContent();

        List<QnADto.Response_AllQnA> dtoList = new ArrayList<>();
        String status;

        for(QnA qna : qnaList){
            if(qna.getReplyText()==null) {
                status = "waiting";
            } else {
                status = "answered";
            }

            //user가 탈퇴하면 null로 표시
            Users qnaUser = qna.getUser();
            String username = null;
            if (qnaUser != null)
                username = qnaUser.getUsername();

            QnADto.Response_AllQnA dto= QnADto.Response_AllQnA.builder()
              .qnaPk(qna.getPk())
              .qnaType(qna.getQnaType())
              .status(status)
              .title(qna.getTitle())
              .userName(username)
              .createAt(qna.getCreateAt())
              .build();
            dtoList.add(dto);
        }
        pageable= PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),Sort.by(Sort.Direction.DESC,"createAt"));
        int start = (int) pageable.getOffset();
        int end=Math.min((start + pageable.getPageSize()),dtoList.size());
        Page<QnADto.Response_AllQnA> qnaAll= new PageImpl<>(dtoList.subList(start,end),pageable,dtoList.size());
        List<QnADto.Response_AllQnA> qnaPage = qnaAll.getContent();
        return qnaPage;
    }*/

    @Transactional(readOnly = true)
    public List<QnADto.Response_AllQnA> all_QnA(QnARequestType category){
        List<QnA> qnaList;

        //카테고리 안넣으면 전부 다 불러옴
        if (category == null)
            qnaList = qnaRepository.findAll();
        else if(category == QnARequestType.RECENT)
            qnaList= qnaRepository.findAllByOrderByCreateAt();
        else if(category==QnARequestType.ANSWERED){
            qnaList=qnaRepository.findAllByReplyTextIsNotNull();
        }else {
            QnAType qnaType = QnAType.valueOf(category.toString());
            qnaList = qnaRepository.findByQnaType(qnaType);
        }

        List<QnADto.Response_AllQnA> dtoList = new ArrayList<>();

        for(QnA qna : qnaList){
            String status;

            if(qna.getReplyText()==null) {
                status = "waiting";
            } else {
                status = "answered";
            }

            //user가 탈퇴하면 null로 표시
            Users qnaUser = qna.getUser();
            String username = null;
            if (qnaUser != null)
                username = qnaUser.getUsername();

            QnADto.Response_AllQnA dto= QnADto.Response_AllQnA.builder()
              .qnaPk(qna.getPk())
              .qnaType(qna.getQnaType())
              .status(status)
              .title(qna.getTitle())
              .userName(username)
              .createAt(qna.getCreateAt())
              .build();

            dtoList.add(dto);
        }

        return dtoList;
    }



    public void reply_QnA(Long QnA_pk, String reply){
        qnaRepository.findById(QnA_pk)
          .orElseThrow(() -> new NotFoundException("일치하는 QnA가 없습니다"))
          .makeReply(reply);
    }
    public void update_Reply(Long QnA_pk, String reply){
        qnaRepository.findById(QnA_pk)
          .orElseThrow(() -> new NotFoundException("일치하는 QnA가 없습니다"))
          .editReply(reply);
    }
    public void delete_Reply(Long QnA_pk){
        qnaRepository.findById(QnA_pk)
          .orElseThrow(() -> new NotFoundException("일치하는 QnA가 없습니다"))
          .deleteReply();
    }
}