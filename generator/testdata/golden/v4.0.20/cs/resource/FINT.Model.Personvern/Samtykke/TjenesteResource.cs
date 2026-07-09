using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Personvern.Samtykke
{

    public class TjenesteResource 
    {

    
        public string Navn { get; set; }
        public DateTime? Slettet { get; set; }
        public Identifikator SystemId { get; set; }
        
        public TjenesteResource()
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
     
            

        public void AddBehandling(Link link)
        {
            AddLink("behandling", link);
        }
    }
}
