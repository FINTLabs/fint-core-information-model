using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Ressurs.Eiendel
{

    public class ApplikasjonResource 
    {

    
        public string Beskrivelse { get; set; }
        public Periode Gyldighetsperiode { get; set; }
        public string Navn { get; set; }
        public Identifikator SystemId { get; set; }
        
        public ApplikasjonResource()
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
     
            

        public void AddPlattform(Link link)
        {
            AddLink("plattform", link);
        }

        public void AddRessurs(Link link)
        {
            AddLink("ressurs", link);
        }

        public void AddApplikasjonskategori(Link link)
        {
            AddLink("applikasjonskategori", link);
        }
    }
}
