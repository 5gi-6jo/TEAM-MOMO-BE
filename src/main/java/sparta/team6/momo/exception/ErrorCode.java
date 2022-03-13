package sparta.team6.momo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

//에러 형식을 Enum 클래스로 정의합니다
//개발자가 정의한 새로운 Exception을 모두 이 곳에서 관리하고 재사용 할 수 있습니다
@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다"),

    MISMATCH_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰의 유저 정보가 일치하지 않습니다"),
    FILE_SIZE_EXCEED(BAD_REQUEST, "이미지 파일 업로드 용량 초과(1MB 제한)"),

    FILE_CONVERT_ERROR(BAD_REQUEST, "파일 변환 중 에러가 발생하였습니다"),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),

    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "현재 내 계정 정보가 존재하지 않습니다"),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    PLAN_NOT_FOUND(NOT_FOUND, "해당 모임 정보를 찾을 수 없습니다"),

    IMAGE_NOT_FOUNT(NOT_FOUND, "해당 이미지 정보를 찾을 수 없습니다"),

    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "로그아웃 된 사용자입니다"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),

    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
