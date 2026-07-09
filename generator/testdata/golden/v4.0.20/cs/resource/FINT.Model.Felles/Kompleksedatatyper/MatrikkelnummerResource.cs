using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

namespace FINT.Model.Felles.Kompleksedatatyper
{

    public class MatrikkelnummerResource 
    {

    
        public AdresseResource Adresse { get; set; }
        public string Bruksnummer { get; set; }
        public string Festenummer { get; set; }
        public string Gardsnummer { get; set; }
        public string Seksjonsnummer { get; set; }
        
        public MatrikkelnummerResource()
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
     
            

        public void AddKommunenummer(Link link)
        {
            AddLink("kommunenummer", link);
        }
    }
}
