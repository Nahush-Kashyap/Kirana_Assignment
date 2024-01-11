package learning.kirana_stores.services;


import learning.kirana_stores.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
public class TransactionService {

    private final StoreRepository storeRepository;

    @Autowired
    public TransactionService(StoreRepository storeRepository){
        this.storeRepository = storeRepository;
    }
}
