using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Timeplan
{

    public class RomResource 
    {

    
        public string Navn { get; set; }
        public Identifikator SystemId { get; set; }
        
        public RomResource()
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
     
            

        public void AddTime(Link link)
        {
            AddLink("time", link);
        }

        public void AddEksamen(Link link)
        {
            AddLink("eksamen", link);
        }
    }
}
