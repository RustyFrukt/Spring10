package ru.geekbrains.hometask10.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.hometask10.entity.ReaderEntity;
import ru.geekbrains.hometask10.repository.ReaderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;
    @Autowired
    public ReaderServiceImpl(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    @Override
    public Optional<ReaderEntity> getReaderById(long id) {
        return readerRepository.findById(id);
    }

    @Override
    public ReaderEntity addReader(ReaderEntity reader) {
        return readerRepository.save(reader);
    }

    @Override
    public Optional<ReaderEntity> updateReader(long id, ReaderEntity reader) {
        readerRepository.findById(id) // returns Optional<Reader>
                .ifPresent(reader1 -> {
                    reader1.setName(reader.getName());
                    readerRepository.save(reader1);
                });
        return readerRepository.findById(id);
    }

    @Override
    public void deleteReader(long id) {
        readerRepository.deleteById(id);
    }

    @Override
    public List<ReaderEntity> getAllReaders() {
        return readerRepository.findAll();
    }

}
