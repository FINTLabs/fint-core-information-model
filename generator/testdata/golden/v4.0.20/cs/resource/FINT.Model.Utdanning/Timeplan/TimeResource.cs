using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Timeplan
{

    public class TimeResource 
    {

    
        public string Beskrivelse { get; set; }
        public string Navn { get; set; }
        public Identifikator SystemId { get; set; }
        public Periode Tidsrom { get; set; }
        
        public TimeResource()
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
     
            

        public void AddUndervisningsgruppe(Link link)
        {
            AddLink("undervisningsgruppe", link);
        }

        public void AddUndervisningsforhold(Link link)
        {
            AddLink("undervisningsforhold", link);
        }

        public void AddRom(Link link)
        {
            AddLink("rom", link);
        }
    }
}
