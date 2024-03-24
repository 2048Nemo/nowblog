package top.rabbitbyte.nowblog.mapper.elasticsearch;

import org.elasticsearch.search.SearchHits;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import top.rabbitbyte.nowblog.entity.DiscussPost;

import java.awt.print.Book;

@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost,Integer> {

    @Highlight(fields = {
            @HighlightField(name = "name"),
            @HighlightField(name = "summary")
    })
    SearchHits<DiscussPost> findByNameOrSummary(String text, String summary);
}

