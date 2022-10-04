package com.gosari.repick_project.markdown;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

@Component /*빈(bean,자바객체)으로 등록*/
public class CommonUtil {

    //markdown메서드는 마크다운텍스트를 HTML 문서로 변환하여 리턴함
    public String markdown(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}
