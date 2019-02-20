package web;

import org.apache.commons.lang3.StringUtils;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.File;
import java.util.List;

@LocalBean
@Stateless
public class PdfManager {

    @PersistenceContext(unitName = "persistenceUnitName")
    private EntityManager entityManager;

    private int flag;
    private int flag2;



    public boolean create(PdfEntity pdfEntity){

        flag = 0;

        if(flag2 == 1){
            System.out.println("===== ===============" + flag2);
            return false;
        }

        if(!checkValid(pdfEntity)){
            flag = 1;
            return false;
        }

        PdfEntity existing = entityManager.find(PdfEntity.class, pdfEntity.getId());
        if(existing != null){
            flag = 1;
            return false;
        }

        entityManager.persist(pdfEntity);
        flag2 = 0;

        return true;
    }

    public boolean delete(String id){

        File file = new File("C:\\Users\\HP\\Desktop\\Data\\" + id);
        file.delete();

        if(StringUtils.isEmpty(id)){
            return false;
        }

        PdfEntity existing = entityManager.find(PdfEntity.class, id);
        if(existing == null){
            return false;
        }

        entityManager.remove(existing);
        return true;
    }

    private boolean checkValid(PdfEntity pdfEntity){

        return pdfEntity != null&&
                !StringUtils.isEmpty(pdfEntity.getId());
    }


    public List<PdfEntity> readList(int offset, int limit){

        TypedQuery<PdfEntity> query =
                entityManager.createQuery("select entity from PdfEntity entity",
                        PdfEntity.class);

        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }



    public void setFlag2(int flag2) {
        this.flag2 = flag2;
    }

    public int getFlag() {
        return flag;
    }

}
