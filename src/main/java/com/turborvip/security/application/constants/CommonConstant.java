package doctintuc.com.websitedoctintuc.application.constants;

import java.text.SimpleDateFormat;

public class CommonConstant {

    public static final class ClassName{
        public  static String USER_CLASS_NAME = "User";
        public  static String CATEGORY_CLASS_NAME = "Category";
        public  static String NEWS_CLASS_NAME = "News";

    }
    public static int SIZE_OFF_PAGE = 10;
    public static final String SORT_ASC = "ASC";
    public static final String SORT_DESC = "DESC";
    public static final String ROLE_SUPER_ADMIN = "super_admin";
    public static final String ROLE_USER="user";
    public static final String ROLE__ADMIN="admin";

    public static final String SORT_BY_TIME = "create_date";
    public static final String SORT_BY_TIME2 = "createDate";
    public static final String FORMAT_DATE_PATTERN = "dd/MM/yyyy";
    public static final String FORMAT_DATE_PATTERN_DETAIL = "dd/MM/yyyy HH:mm:ss";
    public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat(FORMAT_DATE_PATTERN);
    public static final SimpleDateFormat FORMAT_DATE_DETAIL = new SimpleDateFormat(FORMAT_DATE_PATTERN_DETAIL);
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NEWS = "news_id";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_USER = "user_id";
    public static final String COLUMN_COMMENT_ID = "comment_id";


}
