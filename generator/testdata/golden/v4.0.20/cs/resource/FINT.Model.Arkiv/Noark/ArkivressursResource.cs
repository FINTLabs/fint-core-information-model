using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Arkiv.Noark
{

    public class ArkivressursResource 
    {

    
        public Identifikator KildesystemId { get; set; }
        public Identifikator SystemId { get; set; }
        
        public ArkivressursResource()
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
     
            

        public void AddPersonalressurs(Link link)
        {
            AddLink("personalressurs", link);
        }

        public void AddAutorisasjon(Link link)
        {
            AddLink("autorisasjon", link);
        }

        public void AddTilgang(Link link)
        {
            AddLink("tilgang", link);
        }
    }
}
