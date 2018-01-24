package com.moocall.moocall.url;

public class FetchAnimalListUrl extends UrlAbstract {
    private Integer bottomFilter;
    private Integer topFilter;
    private Integer type;

    public FetchAnimalListUrl(Integer type, Integer topFilter, Integer bottomFilter) {
        this.type = type;
        this.topFilter = topFilter;
        this.bottomFilter = bottomFilter;
        setStringUnsignedPart("/mobile-api/index/index/model/cow/method/fetch-cattle-list2/ts/");
    }

    protected String createDynamicPart() {
        String filter = "";
        String cattleType = "";
        System.out.println("Daaaanilooo: " + this.type.toString() + " : " + this.topFilter.toString() + " : " + this.bottomFilter.toString());
        if (this.type.intValue() < 5) {
            switch (this.bottomFilter.intValue()) {
                case 1:
                    cattleType = "0";
                    break;
                case 2:
                    cattleType = "1";
                    break;
                case 3:
                    cattleType = "3";
                    break;
            }
        }
        switch (this.type.intValue()) {
            case 1:
                switch (this.topFilter.intValue()) {
                    case 1:
                        filter = "incalf";
                        break;
                    case 2:
                        filter = "due7";
                        break;
                    case 3:
                        filter = "calved90";
                        break;
                    default:
                        break;
                }
            case 2:
                switch (this.topFilter.intValue()) {
                    case 1:
                        filter = "cycle90";
                        break;
                    case 2:
                        filter = "cycle1724";
                        break;
                    case 3:
                        filter = "cycle3845";
                        break;
                    default:
                        break;
                }
            case 3:
                switch (this.topFilter.intValue()) {
                    case 1:
                        filter = "inheat90";
                        break;
                    case 2:
                        filter = "inheat12";
                        break;
                    default:
                        break;
                }
            case 4:
                switch (this.topFilter.intValue()) {
                    case 1:
                        filter = "insemenated90";
                        break;
                    case 2:
                        filter = "insemenated1724";
                        break;
                    case 3:
                        filter = "insemenated3845";
                        break;
                    default:
                        break;
                }
            case 5:
                switch (this.topFilter.intValue()) {
                    case 1:
                        filter = "byage";
                        break;
                    case 2:
                        filter = "byweight";
                        break;
                    case 3:
                        filter = "bytag";
                        break;
                }
                switch (this.bottomFilter.intValue()) {
                    case 1:
                        cattleType = "42";
                        break;
                    case 2:
                        cattleType = "4";
                        break;
                    case 3:
                        cattleType = "2";
                        break;
                    case 4:
                        cattleType = "10";
                        break;
                    default:
                        break;
                }
            case 6:
                switch (this.topFilter.intValue()) {
                    case 1:
                        filter = "byage";
                        break;
                    case 2:
                        filter = "byweight";
                        break;
                    case 3:
                        filter = "bytag";
                        break;
                }
                switch (this.bottomFilter.intValue()) {
                    case 1:
                        cattleType = "78";
                        break;
                    case 2:
                        cattleType = "7";
                        break;
                    case 3:
                        cattleType = "8";
                        break;
                    default:
                        break;
                }
            case 7:
                switch (this.topFilter.intValue()) {
                    case 1:
                        filter = "byage";
                        break;
                    case 2:
                        filter = "byweight";
                        break;
                    case 3:
                        filter = "bytag";
                        break;
                }
                switch (this.bottomFilter.intValue()) {
                    case 1:
                        cattleType = "56";
                        break;
                    case 2:
                        cattleType = "5";
                        break;
                    case 3:
                        cattleType = "6";
                        break;
                    default:
                        break;
                }
            case 8:
                switch (this.topFilter.intValue()) {
                    case 1:
                        filter = "dateculled";
                        break;
                    case 2:
                        filter = "datesold";
                        break;
                    case 3:
                        filter = "bytag";
                        break;
                }
                switch (this.bottomFilter.intValue()) {
                    case 1:
                        cattleType = "111";
                        break;
                    case 2:
                        cattleType = "101";
                        break;
                    case 3:
                        cattleType = "178";
                        break;
                    case 4:
                        cattleType = "102";
                        break;
                    default:
                        break;
                }
            case 9:
                switch (this.topFilter.intValue()) {
                    case 1:
                        filter = "byage";
                        break;
                    case 2:
                        filter = "byweight";
                        break;
                    case 3:
                        filter = "bytag";
                        break;
                }
                switch (this.bottomFilter.intValue()) {
                    case 1:
                        cattleType = "0";
                        break;
                    case 2:
                        cattleType = "1";
                        break;
                    case 3:
                        cattleType = "3";
                        break;
                    default:
                        break;
                }
        }
        return "/cattle_type/" + cattleType + "/filter/" + filter;
    }
}
