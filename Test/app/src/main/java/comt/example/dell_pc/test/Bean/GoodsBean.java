package comt.example.dell_pc.test.Bean;

import java.util.List;

public class GoodsBean {


    /**
     * count : 6
     * datas : [{"Id":6,"Name":"大田螺","GoodsId":"6","Size":5,"Price":0},{"Id":5,"Name":"小田螺","GoodsId":"5","Size":4,"Price":0},{"Id":4,"Name":"田螺","GoodsId":"4","Size":8,"Price":0},{"Id":3,"Name":"龙虾","GoodsId":"3","Size":9,"Price":0},{"Id":2,"Name":"大龙虾","GoodsId":"2","Size":1,"Price":0},{"Id":1,"Name":"小龙虾","GoodsId":"1","Size":10,"Price":0}]
     * status : 200
     * time : 2018-08-15 10:44:29
     */

    private int count;
    private int status;
    private String time;
    private List<DatasBean> datas;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * Id : 6
         * Name : 大田螺
         * GoodsId : 6
         * Size : 5
         * Price : 0
         */

        private int Id;
        private String Name;
        private String GoodsId;
        private int Size;
        private int Price;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getGoodsId() {
            return GoodsId;
        }

        public void setGoodsId(String GoodsId) {
            this.GoodsId = GoodsId;
        }

        public int getSize() {
            return Size;
        }

        public void setSize(int Size) {
            this.Size = Size;
        }

        public int getPrice() {
            return Price;
        }

        public void setPrice(int Price) {
            this.Price = Price;
        }
    }
}
