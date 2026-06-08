import java.io.InputStream;
import java.util.Properties;

/**
 * DB 접속 정보를 외부 설정 파일(db.properties)에서 읽어 제공하는 클래스.
 * <p>자격증명을 소스 코드에 하드코딩하지 않기 위한 창구 역할을 합니다.</p>
 * <p>db.properties는 .gitignore에 등록되어 버전 관리에서 제외됩니다.</p>
 */
public class DBConfig {
    // 파일에서 읽은 키-값을 담아두는 통
    private static final Properties props = new Properties();

    // 클래스가 처음 로딩될 때 딱 한 번 실행 → db.properties를 읽어 props에 채움
    static {
        try (InputStream in = DBConfig.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            } else {
                System.err.println("[오류] db.properties 파일을 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            System.err.println("[오류] DB 설정 로드에 실패했습니다.");
        }
    }

    /** @return DB 접속 URL */
    public static String getUrl()      { return props.getProperty("db.url"); }

    /** @return DB 사용자명 */
    public static String getUser()     { return props.getProperty("db.user"); }

    /** @return DB 비밀번호 */
    public static String getPassword() { return props.getProperty("db.password"); }
}