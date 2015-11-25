package com.fasterxml.jackson.failing;

import com.fasterxml.jackson.annotation.*;

import com.fasterxml.jackson.databind.*;

public class DelegatingExternalProperty1003Test extends BaseMapTest
{
    static class HeroBattle {

        private final Hero hero;

        private HeroBattle(Hero hero) {
            if (hero == null) throw new Error();
            this.hero = hero;
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "heroType")
        public Hero getHero() {
            return hero;
        }

        @JsonCreator
        static HeroBattle fromJson(Delegate json) {
            return new HeroBattle(json.hero);
        }
    }

    static class Delegate {
        @JsonProperty
        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "heroType")
        public Hero hero;
    }

    public interface Hero { }

    static class Superman implements Hero {
        public String getName() {
            return "superman";
        }
    }    

    public void testExtrnalPropertyDelegatingCreator() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();

        final String json = mapper.writeValueAsString(new HeroBattle(new Superman()));

//System.err.println("JSON: "+json);        
        final HeroBattle battle = mapper.readValue(json, HeroBattle.class);

        assert battle.getHero() instanceof Superman;
    }

    
}