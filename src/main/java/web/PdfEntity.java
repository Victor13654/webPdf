package web;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "PDF_TABLE")
public class PdfEntity {

    @Id
    private String id;

    @Column
    private long size;

    @Column
    private String description;

    @Column
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Pdf toDto(){

        Pdf pdf = new Pdf();
        pdf.setId(this.getId());
        pdf.setDate(this.getDate());
        pdf.setDescription(this.getDescription());
        pdf.setSize(this.getSize());

        return pdf;
    }

    public void fromDto(Pdf pdf){

        this.setId(pdf.getId());
        this.setDate(pdf.getDate());
        this.setDescription(pdf.getDescription());
        this.setSize(pdf.getSize());
    }
}
