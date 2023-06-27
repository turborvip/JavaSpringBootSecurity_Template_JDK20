package com.turborvip.security.application.constants;

import java.text.SimpleDateFormat;

public class CommonConstant {

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



}
