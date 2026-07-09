using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Ressurs.Datautstyr
{

    public class EnhetsgruppeResource 
    {

    
        public string Navn { get; set; }
        public Identifikator SystemId { get; set; }
        
        public EnhetsgruppeResource()
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
     
            

        public void AddOrganisasjonsenhet(Link link)
        {
            AddLink("organisasjonsenhet", link);
        }

        public void AddEnhetstype(Link link)
        {
            AddLink("enhetstype", link);
        }

        public void AddPlattform(Link link)
        {
            AddLink("plattform", link);
        }

        public void AddEnhetsgruppemedlemskap(Link link)
        {
            AddLink("enhetsgruppemedlemskap", link);
        }
    }
}
