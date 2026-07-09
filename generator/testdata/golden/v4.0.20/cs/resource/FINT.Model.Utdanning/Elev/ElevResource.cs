using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Elev
{

    public class ElevResource 
    {

    
        public Identifikator Brukernavn { get; set; }
        public Identifikator Elevnummer { get; set; }
        public Identifikator Feidenavn { get; set; }
        public bool? Gjest { get; set; }
        public AdresseResource Hybeladresse { get; set; }
        public Kontaktinformasjon Kontaktinformasjon { get; set; }
        public Identifikator SystemId { get; set; }
        
        public ElevResource()
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

        public void AddElevforhold(Link link)
        {
            AddLink("elevforhold", link);
        }
    }
}
