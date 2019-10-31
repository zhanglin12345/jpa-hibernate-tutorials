package com.example.jpa;

import com.example.jpa.model.Comment;
import com.example.jpa.model.Post;
import com.example.jpa.repository.PostRepository;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaOneToManyDemoApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
    private PostRepository postRepository;

	@Test
    public void should_insert() {
        List<Comment> comments = new ArrayList<>();
        comments.add(Comment.builder().text("text1").build());
        comments.add(Comment.builder().text("text2").build());
        Post post = Post.builder().title("t").description("d").content("c").comments(comments).build();
        comments.forEach(x->x.setPost(post));
        postRepository.save(post);


        List<Post> savedPosts = postRepository.findAll();
        assertEquals(1, savedPosts.size());
        assertEquals(2, savedPosts.get(0).getComments().size());


        Post savedPost = savedPosts.get(0);
        Comment comment = savedPost.getComments().get(0);
        Comment deletedComment = savedPost.getComments().get(1);
        deletedComment.setPost(null);
        savedPost.getComments().clear();

        savedPost.getComments().add(comment);
        comment.setPost(savedPost);
        postRepository.save(savedPost);

        List<Post> finalPosts = postRepository.findAll();
        assertEquals(1, finalPosts.size());
        assertEquals(1, finalPosts.get(0).getComments().size());
    }

}
