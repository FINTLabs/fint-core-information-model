using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Arkiv.Noark
{

    public class AutorisasjonResource 
    {

    
        public Identifikator SystemId { get; set; }
        
        public AutorisasjonResource()
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
     
            

        public void AddTilgangsrestriksjon(Link link)
        {
            AddLink("tilgangsrestriksjon", link);
        }

        public void AddAdministrativenhet(Link link)
        {
            AddLink("administrativenhet", link);
        }

        public void AddArkivressurs(Link link)
        {
            AddLink("arkivressurs", link);
        }
    }
}
