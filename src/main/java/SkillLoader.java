import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SkillLoader {
    private List<Skill> skills;

    public SkillLoader(){
        this.skills = new ArrayList<>();
        loadSkillsList();
    }

    private void loadSkillsList(){
        try{
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("skills.json");

            if(inputStream == null){
                System.out.println("Не удалось подгрузить файл skills.json");
            }
            JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
            var skillsArray = jsonObject.getAsJsonArray("skills");

            Gson gson = new Gson();
            for(var skillElement : skillsArray){
                JsonObject skillJson = skillElement.getAsJsonObject();
                Skill skill = gson.fromJson(skillJson, Skill.class);
                skills.add(skill);
            }

            inputStream.close();
            System.out.println("Загружено умений: " + skills.size());
        }catch (Exception e){
            System.out.println("Ошибка загрузки данных из JSON" + e.getMessage());
        }
    }

    public List<Skill> getSkills() {
        return skills;
    }
}
