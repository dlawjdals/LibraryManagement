import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DBConfig 자격증명 외부화 테스트")
class DBConfigTest {

    @Test
    @DisplayName("db.properties에서 URL을 정상적으로 읽어와야 한다")
    void getUrl_shouldLoadFromProperties() {
        String url = DBConfig.getUrl();

        assertNotNull(url, "URL이 로드되어야 합니다 (db.properties 위치 확인).");
        assertTrue(url.startsWith("jdbc:mariadb://"),
                "MariaDB JDBC URL 형식이어야 합니다.");
        System.out.println("[통과] URL 로드 확인 - " + url);
    }

    @Test
    @DisplayName("user 값이 null이 아니어야 한다")
    void getUser_shouldNotBeNull() {
        String user = DBConfig.getUser();

        assertNotNull(user, "DB 사용자명이 로드되어야 합니다.");
        assertFalse(user.isBlank(), "사용자명이 비어 있으면 안 됩니다.");
        System.out.println("[통과] User 로드 확인 - " + user);
    }

    @Test
    @DisplayName("password 값이 null이 아니어야 한다")
    void getPassword_shouldNotBeNull() {
        String password = DBConfig.getPassword();

        assertNotNull(password, "DB 비밀번호가 로드되어야 합니다.");
        assertFalse(password.isBlank(), "비밀번호가 비어 있으면 안 됩니다.");
        // 비밀번호 값 자체는 콘솔에 찍지 않음 (보안)
        System.out.println("[통과] Password 로드 확인 - (값 출력 생략)");
    }

    @Test
    @DisplayName("운영 코드에 하드코딩된 PASSWORD 상수가 남아있지 않아야 한다")
    void credentials_shouldNotBeHardcodedInSource() {
        // 리플렉션으로 PASSWORD 상수 존재 여부 확인
        // 상수가 삭제되었다면 NoSuchFieldException이 발생해야 정상
        assertThrows(NoSuchFieldException.class,
                () -> DBconn.class.getDeclaredField("PASSWORD"),
                "DBconn에 하드코딩된 PASSWORD 상수가 없어야 합니다.");

        assertThrows(NoSuchFieldException.class,
                () -> LibraryRepository.class.getDeclaredField("PASSWORD"),
                "LibraryRepository에 하드코딩된 PASSWORD 상수가 없어야 합니다.");

        System.out.println("[통과] 운영 코드에 하드코딩된 자격증명 없음 확인");
    }

    @Test
    @DisplayName("DBConfig 설정으로 실제 DB 연결이 되어야 한다 (통합)")
    void connection_shouldWorkWithDBConfig() {
        // DBConfig가 제공한 정보로 실제 연결이 성립하는지 확인
        try (var conn = java.sql.DriverManager.getConnection(
                DBConfig.getUrl(), DBConfig.getUser(), DBConfig.getPassword())) {

            assertNotNull(conn, "DBConfig 정보로 연결 객체가 생성되어야 합니다.");
            assertTrue(conn.isValid(2), "연결이 유효해야 합니다.");
            System.out.println("[통과] DBConfig 기반 DB 연결 성공");
        } catch (java.sql.SQLException e) {
            fail("DBConfig 정보로 DB 연결에 실패했습니다: " + e.getMessage());
        }
    }
}