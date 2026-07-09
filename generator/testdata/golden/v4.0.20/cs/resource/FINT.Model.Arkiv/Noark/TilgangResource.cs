using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Arkiv.Noark
{

    public class TilgangResource 
    {

    
        public Identifikator SystemId { get; set; }
        public string Tittel { get; set; }
        
        public TilgangResource()
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
     
            

        public void AddRolle(Link link)
        {
            AddLink("rolle", link);
        }

        public void AddAdministrativEnhet(Link link)
        {
            AddLink("administrativEnhet", link);
        }

        public void AddArkivdel(Link link)
        {
            AddLink("arkivdel", link);
        }

        public void AddArkivressurs(Link link)
        {
            AddLink("arkivressurs", link);
        }
    }
}
