using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Larling
{

    public class AvlagtProveResource 
    {

    
        public DateTime? Provedato { get; set; }
        public Identifikator SystemId { get; set; }
        
        public AvlagtProveResource()
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
     
            

        public void AddProvestatus(Link link)
        {
            AddLink("provestatus", link);
        }

        public void AddLarling(Link link)
        {
            AddLink("larling", link);
        }

        public void AddFullfortkode(Link link)
        {
            AddLink("fullfortkode", link);
        }

        public void AddBrevtype(Link link)
        {
            AddLink("brevtype", link);
        }

        public void AddBevistype(Link link)
        {
            AddLink("bevistype", link);
        }
    }
}
