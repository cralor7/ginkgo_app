package com.qk.module;

import java.util.List;

/**
 * @author fengyezong & cuiweilong
 * @date 2018/8/13
 * 仓库实体类
 */
public class Stock {

    private String code;
    private String message;
    private String token;
    private List<Data> data;

    public class Data{

        private String id;
        private String stockcode;
        private String stockname;

        private Office office;

        public class Office{

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Office(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return "Office{" +
                        "name='" + name + '\'' +
                        '}';
            }
        }

        public Data(String id, String stockcode, String stockname, Office office) {
            this.id = id;
            this.stockcode = stockcode;
            this.stockname = stockname;
            this.office = office;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStockcode() {
            return stockcode;
        }

        public void setStockcode(String stockcode) {
            this.stockcode = stockcode;
        }

        public String getStockname() {
            return stockname;
        }

        public void setStockname(String stockname) {
            this.stockname = stockname;
        }

        public Office getOffice() {
            return office;
        }

        public void setOffice(Office office) {
            this.office = office;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "id='" + id + '\'' +
                    ", stockcode='" + stockcode + '\'' +
                    ", stockname='" + stockname + '\'' +
                    ", office=" + office +
                    '}';
        }
    }



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Stock(String code, String message, String token, List<Data> data) {
        this.code = code;
        this.message = message;
        this.token = token;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", token='" + token + '\'' +
                ", data=" + data +
                '}';
    }
}
