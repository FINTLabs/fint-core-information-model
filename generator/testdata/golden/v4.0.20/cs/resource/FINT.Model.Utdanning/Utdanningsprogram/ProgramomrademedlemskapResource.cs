using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Utdanning.Basisklasser;

namespace FINT.Model.Utdanning.Utdanningsprogram
{

    public class ProgramomrademedlemskapResource : Gruppemedlemskap 
    {

    
        public ProgramomrademedlemskapResource()
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
     
            

        public void AddElevforhold(Link link)
        {
            AddLink("elevforhold", link);
        }

        public void AddProgramomrade(Link link)
        {
            AddLink("programomrade", link);
        }
    }
}
