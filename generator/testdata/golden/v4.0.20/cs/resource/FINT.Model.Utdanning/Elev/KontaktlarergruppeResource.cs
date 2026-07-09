using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Utdanning.Basisklasser;

namespace FINT.Model.Utdanning.Elev
{

    public class KontaktlarergruppeResource : Gruppe 
    {

    
        public KontaktlarergruppeResource()
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
     
            

        public void AddKlasse(Link link)
        {
            AddLink("klasse", link);
        }

        public void AddTermin(Link link)
        {
            AddLink("termin", link);
        }

        public void AddSkole(Link link)
        {
            AddLink("skole", link);
        }

        public void AddSkolear(Link link)
        {
            AddLink("skolear", link);
        }

        public void AddUndervisningsforhold(Link link)
        {
            AddLink("undervisningsforhold", link);
        }

        public void AddGruppemedlemskap(Link link)
        {
            AddLink("gruppemedlemskap", link);
        }
    }
}
