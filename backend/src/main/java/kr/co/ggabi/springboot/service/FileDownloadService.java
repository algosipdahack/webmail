package kr.co.ggabi.springboot.service;

import kr.co.ggabi.springboot.domain.users.Member;
import kr.co.ggabi.springboot.jwt.TokenProvider;
import kr.co.ggabi.springboot.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileDownloadService {

    private final MembersRepository membersRepository;

    public Resource loadFileAsResource(String username, int idx, String filename, String mailBox){
        Member member = membersRepository.findByUsername(username).get();
        System.out.println(filename);
        long uid = member.getId();
        try{
            String path = "./downloads" + File.separator + mailBox + File.separator + Long.toString(uid) + File.separator + Integer.toString(idx) + File.separator + filename;
            Path filePath = Paths.get(path);
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println("cannot found resource");
        return null;
    }

    public Resource loadFileForBoard(Long bid, Long postlistId, String filename){
        try{
            String path = "./downloads" + File.separator + "board" + File.separator + Long.toString(bid) + File.separator + Long.toString(postlistId) + File.separator + filename;
            Path filePath = Paths.get(path);
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println("cannot found resource");
        return null;
    }
}
