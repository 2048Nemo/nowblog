package top.rabbitbyte.nowblog.entity;

import lombok.Data;

@Data
public class Page {
    // 当前页
    private int current = 1 ;
    // 每页显示条数
    private int limit  = 10;
    //总数量
    private int rows  ;

    // 查询路径(拼接分页参数)
    private String path;

    public void setCurrent(int current) {
        if (current >= 1){
            this.current = current;
        }
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100){
            this.limit = limit;
        }
    }

    public void setRows(int rows) {
        if (rows >= 0){
            this.rows = rows;
        }
    }

    /**
     * 计算查询起始位置
     * @return
     */
    public  int  getOffset(){
        return (current - 1) * limit;
    }

    /**
     * 计算总的分页数量
     * @return
     */
    public int getTotal() {
        if (rows % limit == 0){
            return rows/limit;
        }else {
            return rows/limit + 1;
        }
    }
    public int getFrom(){
        return current - 2 > 0? current - 2 : 1;
    }

    public int getTo(){
        return current + 2 > getTotal()? getTotal() : current + 2;
    }
}
