import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconn {
    // [보안 개선] 하드코딩된 URL/USER/PASSWORD 상수 제거
    //            → 접속 정보는 DBConfig를 통해 db.properties에서 읽어옴

    /**
     * 데이터베이스 연결 객체를 반환합니다.
     * <p>접속 정보는 소스 코드에 두지 않고 DBConfig(외부 설정 파일)에서 가져옵니다.</p>
     * @return Connection 객체 (실패 시 null)
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // 1. 드라이버 로드
            Class.forName("org.mariadb.jdbc.Driver");
//            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // 2. DBConfig에서 접속 정보를 받아 연결 수행 (하드코딩 제거)
            conn = DriverManager.getConnection(
                    DBConfig.getUrl(),
                    DBConfig.getUser(),
                    DBConfig.getPassword());
            System.out.println("[시스템] MariaDB 연결 성공!");

        } catch (ClassNotFoundException e) {
            // [보안] 상세 예외 메시지 노출 최소화
            System.err.println("[오류] 드라이버를 찾을 수 없습니다.");
        } catch (SQLException e) {
            System.err.println("[오류] DB 연결에 실패했습니다.");
        }
        return conn;
    }

    // 간단한 연결 테스트용 main
    public static void main(String[] args) {
        Connection testConn = getConnection();
        if (testConn != null) {
            try {
                testConn.close(); // 테스트 후 닫기
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}