using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Okonomi.Regnskap
{

    public class LeverandorResource 
    {

    
        public string Kontonummer { get; set; }
        public Identifikator Leverandornummer { get; set; }
        public Identifikator SystemId { get; set; }
        
        public LeverandorResource()
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

        public void AddLeverandorgruppe(Link link)
        {
            AddLink("leverandorgruppe", link);
        }

        public void AddVirksomhet(Link link)
        {
            AddLink("virksomhet", link);
        }
    }
}
