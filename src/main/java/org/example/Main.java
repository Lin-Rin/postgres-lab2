package org.example;

import org.example.dao.InPlaceUpdate;
import org.example.dao.LostUpdate;
import org.example.dao.OptimisticConcurrencyControlUpdate;
import org.example.dao.RowDao;
import org.example.dao.RowLevelLockingUpdate;
import org.example.model.Row;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        RowDao rowDao = new RowDao();
        List<Thread> threads = new ArrayList<>();

        LostUpdate lostUpdate = new LostUpdate();
        InPlaceUpdate inPlaceUpdate = new InPlaceUpdate();
        RowLevelLockingUpdate lockingUpdate = new RowLevelLockingUpdate();
        OptimisticConcurrencyControlUpdate optimisticUpdate = new OptimisticConcurrencyControlUpdate();

        Row save = rowDao.save(new Row(0L, 0L));

        long start = System.currentTimeMillis();

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                for (int k = 0; k < 1000; k++) {
                    lostUpdate.updateAndIncrement(save);
//                    inPlaceUpdate.updateAndIncrement(save); //73
//                    lockingUpdate.updateAndIncrement(save); // 79
//                    optimisticUpdate.updateAndIncrement(save); // 89
                }
            });

            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long finish = System.currentTimeMillis();
        System.out.println(rowDao.findCounterByUserId(save.getUserId()));
        System.out.println("Time: " + (finish - start) + "ms");
        System.out.println("Time: " + ((finish - start) / 1000) + "seconds");
    }
}

// docker pull postgres

// docker run --name to-remove-db -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres
// docker exec -it to-remove-bd2 psql -U postgres
// user::postgres
// password::password