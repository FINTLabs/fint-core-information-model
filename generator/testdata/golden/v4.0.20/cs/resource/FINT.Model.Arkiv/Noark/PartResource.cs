using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Arkiv.Noark
{

    public class PartResource 
    {

    
        public AdresseResource Adresse { get; set; }
        public string Fodselsnummer { get; set; }
        public Kontaktinformasjon Kontaktinformasjon { get; set; }
        public string Kontaktperson { get; set; }
        public string Organisasjonsnummer { get; set; }
        public string PartNavn { get; set; }
        
        public PartResource()
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
     
            

        public void AddPartRolle(Link link)
        {
            AddLink("partRolle", link);
        }
    }
}
