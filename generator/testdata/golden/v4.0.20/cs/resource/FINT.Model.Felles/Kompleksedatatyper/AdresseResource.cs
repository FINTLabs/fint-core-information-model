using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

namespace FINT.Model.Felles.Kompleksedatatyper
{

    public class AdresseResource 
    {

    
        public List<string> Adresselinje { get; set; }
        public string Postnummer { get; set; }
        public string Poststed { get; set; }
        
        public AdresseResource()
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
     
            

        public void AddLand(Link link)
        {
            AddLink("land", link);
        }
    }
}
