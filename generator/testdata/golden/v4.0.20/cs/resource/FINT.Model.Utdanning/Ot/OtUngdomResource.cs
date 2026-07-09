using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Ot
{

    public class OtUngdomResource 
    {

    
        public Identifikator SystemId { get; set; }
        
        public OtUngdomResource()
        {
            Links = new Dictionary<string, List<Link>>();
        }

        [JsonProperty(PropertyName = "_links")]
        public Dictionary<string, List<Link>> Links { get; private set; }

        protected void AddLink(string key, Link link)
        {
            if (!Links.ContainsKey(key))
            {
                Links.Add(key, new List<Link>());
            }
            Links[key].Add(link);
        }
     
            

        public void AddPerson(Link link)
        {
            AddLink("person", link);
        }

        public void AddStatus(Link link)
        {
            AddLink("status", link);
        }

        public void AddEnhet(Link link)
        {
            AddLink("enhet", link);
        }

        public void AddProgramomrade(Link link)
        {
            AddLink("programomrade", link);
        }
    }
}
